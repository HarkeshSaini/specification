package com.specification.service.service.ollama.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.specification.service.config.HuggingFaceProperties;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.output.FinishReason;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Hugging Face free serverless inference without an API key via the hf-inference router only.
 */
@Slf4j
public class HuggingFaceFreeChatModel implements ChatModel {

    private static final int MAX_LOAD_RETRIES = 3;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final HuggingFaceProperties properties;
    private final String modelName;

    public HuggingFaceFreeChatModel(
            WebClient webClient,
            ObjectMapper objectMapper,
            HuggingFaceProperties properties,
            String modelName) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
        this.properties = properties;
        this.modelName = modelName;
    }

    @Override
    public ChatResponse chat(ChatRequest request) {
        String prompt = extractUserText(request);
        String text = callWithLoadRetry(prompt);
        return ChatResponse.builder()
                .aiMessage(dev.langchain4j.data.message.AiMessage.from(text))
                .finishReason(FinishReason.STOP)
                .build();
    }

    private String callWithLoadRetry(String prompt) {
        RuntimeException last = null;
        for (int attempt = 0; attempt < MAX_LOAD_RETRIES; attempt++) {
            try {
                return callRouter(prompt);
            } catch (ModelLoadingException ex) {
                last = ex;
                long waitMs = Math.min((long) (ex.estimatedSeconds() * 1000L), 20_000L);
                log.info("HuggingFace model loading, retry in {}ms (attempt {}/{})", waitMs, attempt + 1, MAX_LOAD_RETRIES);
                sleepQuietly(waitMs);
            }
        }
        throw last != null ? last : new RuntimeException("HuggingFace free inference failed");
    }

    private String callRouter(String prompt) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", formatRouterModel(modelName));
        body.put("messages", List.of(Map.of("role", "user", "content", prompt)));
        body.put("max_tokens", 512);
        body.put("temperature", 0.7);

        try {
            String raw = webClient.post()
                    .uri(properties.getFreeBaseUrl() + "/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(properties.timeout())
                    .block();
            return parseOpenAiResponse(raw);
        } catch (WebClientResponseException ex) {
            throw userFacingError(
                    "HuggingFace request failed (HTTP " + ex.getStatusCode().value() + "): "
                            + truncate(ex.getResponseBodyAsString()),
                    ex);
        } catch (RuntimeException ex) {
            throw userFacingError(ex.getMessage(), ex);
        } catch (Exception ex) {
            throw userFacingError(ex.getMessage(), ex);
        }
    }

    private RuntimeException userFacingError(String detail, Exception cause) {
        String message = detail != null ? detail : cause.getClass().getSimpleName();
        if (isNetworkError(message, cause)) {
            message = "Cannot reach HuggingFace (network or DNS blocked). "
                    + "Start Ollama locally with: ollama serve";
        } else {
            message = "HuggingFace free inference failed: " + message
                    + ". Try starting Ollama locally, or set HF_API_KEY if HuggingFace is allowed on your network.";
        }
        log.warn("HuggingFace free inference error: {}", message);
        return new RuntimeException(message, cause);
    }

    private static boolean isNetworkError(String message, Exception cause) {
        String combined = (message != null ? message : "") + " " + cause.getMessage();
        String lower = combined.toLowerCase();
        return lower.contains("failed to resolve")
                || lower.contains("unknownhost")
                || lower.contains("connection refused")
                || lower.contains("connection timed out")
                || lower.contains("no route to host")
                || lower.contains("network is unreachable");
    }

    private String parseOpenAiResponse(String raw) {
        try {
            JsonNode root = objectMapper.readTree(raw);
            if (root.has("error")) {
                JsonNode errorNode = root.get("error");
                String error = errorNode.isTextual() ? errorNode.asText() : errorNode.toString();
                if (error.toLowerCase().contains("loading")) {
                    double wait = root.has("estimated_time") ? root.get("estimated_time").asDouble(5.0) : 5.0;
                    throw new ModelLoadingException(wait);
                }
                throw new RuntimeException("HuggingFace router error: " + error);
            }
            JsonNode choices = root.path("choices");
            if (choices.isArray() && !choices.isEmpty()) {
                JsonNode content = choices.get(0).path("message").path("content");
                if (!content.isMissingNode() && content.isTextual()) {
                    return content.asText();
                }
            }
            throw new RuntimeException("Unexpected HuggingFace response: " + truncate(raw));
        } catch (ModelLoadingException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse HuggingFace response", ex);
        }
    }

    private static String formatRouterModel(String model) {
        return model.contains(":hf-inference") ? model : model + ":hf-inference";
    }

    private static String truncate(String text) {
        if (text == null) {
            return "";
        }
        return text.length() > 200 ? text.substring(0, 200) + "…" : text;
    }

    private static String extractUserText(ChatRequest request) {
        List<ChatMessage> messages = request.messages();
        if (messages == null || messages.isEmpty()) {
            throw new IllegalArgumentException("Chat request has no messages");
        }
        List<String> parts = new ArrayList<>();
        for (ChatMessage message : messages) {
            if (message instanceof UserMessage userMessage) {
                parts.add(userMessage.singleText());
            }
        }
        if (parts.isEmpty()) {
            throw new IllegalArgumentException("Chat request has no user message");
        }
        return String.join("\n", parts);
    }

    private static void sleepQuietly(long ms) {
        try {
            Thread.sleep(Math.max(500L, ms));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for HuggingFace model", ex);
        }
    }

    private static final class ModelLoadingException extends RuntimeException {
        private final double estimatedSeconds;

        ModelLoadingException(double estimatedSeconds) {
            super("Model is loading");
            this.estimatedSeconds = estimatedSeconds;
        }

        double estimatedSeconds() {
            return estimatedSeconds;
        }
    }
}
