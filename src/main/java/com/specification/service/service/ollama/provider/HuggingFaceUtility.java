package com.specification.service.service.ollama.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.specification.service.config.HuggingFaceProperties;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * Hugging Face fallback utility.
 * <ul>
 *   <li>With API key: full router at {@code router.huggingface.co/v1}</li>
 *   <li>Without API key: free hf-inference tier (no signup token required)</li>
 * </ul>
 */
@Slf4j
@Component
public class HuggingFaceUtility {

    private final HuggingFaceProperties properties;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public HuggingFaceUtility(
            HuggingFaceProperties properties,
            WebClient.Builder webClientBuilder,
            ObjectMapper objectMapper) {
        this.properties = properties;
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public boolean isEnabled() {
        return properties.isEnabled();
    }

    /** True when HuggingFace fallback is available (with or without API key). */
    public boolean isConfigured() {
        return properties.isEnabled();
    }

    public boolean hasApiKey() {
        return StringUtils.hasText(properties.getApiKey());
    }

    public boolean isFreeMode() {
        return isConfigured() && !hasApiKey();
    }

    public ChatModel buildChatModel(String modelName) {
        String resolved = resolveModelName(modelName);
        if (hasApiKey()) {
            log.debug("Building HuggingFace router model (API key): {}", resolved);
            return OpenAiChatModel.builder()
                    .apiKey(properties.getApiKey())
                    .baseUrl(properties.getBaseUrl())
                    .modelName(resolved)
                    .timeout(properties.timeout())
                    .temperature(0.7)
                    .build();
        }
        String freeModel = formatFreeModel(resolved);
        log.debug("Building HuggingFace free model (no API key): {}", freeModel);
        return new HuggingFaceFreeChatModel(webClient, objectMapper, properties, freeModel);
    }

    public String chat(String prompt, String modelName) {
        return buildChatModel(modelName).chat(prompt);
    }

    public String getDefaultModel() {
        return properties.getDefaultModel();
    }

    public String getBaseUrlForDisplay() {
        return hasApiKey() ? properties.getBaseUrl() : properties.getFreeBaseUrl();
    }

    public String resolveModelName(String model) {
        if (StringUtils.hasText(model) && !"auto".equalsIgnoreCase(model.trim())) {
            String name = model.trim();
            if (hasApiKey()) {
                return name;
            }
            return resolveFreeModelName(name);
        }
        return getDefaultModel();
    }

    private String resolveFreeModelName(String name) {
        String withSuffix = formatFreeModel(name);
        for (String free : listSuggestedModels()) {
            if (free.equalsIgnoreCase(withSuffix) || free.equalsIgnoreCase(name)) {
                return free.replace(":hf-inference", "");
            }
        }
        log.debug("Model '{}' is not on the free HF tier; using {}", name, properties.getFreeDefaultModel());
        return properties.getFreeDefaultModel().replace(":hf-inference", "");
    }

    /**
     * Models on the free hf-inference tier (no API key).
     */
    public List<String> listSuggestedModels() {
        if (hasApiKey()) {
            return List.of(
                    properties.getDefaultModel(),
                    "meta-llama/Meta-Llama-3-8B-Instruct",
                    "mistralai/Mistral-7B-Instruct-v0.2",
                    "google/gemma-2-2b-it"
            );
        }
        return List.of(
                "HuggingFaceTB/SmolLM3-3B:hf-inference",
                "google/gemma-2-2b-it:hf-inference",
                "gpt2:hf-inference"
        );
    }

    private String formatFreeModel(String model) {
        if (model.contains(":hf-inference")) {
            return model;
        }
        return model + ":hf-inference";
    }
}
