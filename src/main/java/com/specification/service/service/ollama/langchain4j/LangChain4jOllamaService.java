package com.specification.service.service.ollama.langchain4j;

import com.specification.service.config.OllamaProperties;
import com.specification.service.service.ollama.AiAgentType;
import com.specification.service.service.ollama.bulkhead.AiBulkheadRegistry;
import com.specification.service.service.ollama.bulkhead.AiBulkheadSupport;
import com.specification.service.service.ollama.bulkhead.AiBulkheadType;
import com.specification.service.service.ollama.provider.AiProviderSelector;
import com.specification.service.service.ollama.provider.AiProviderType;
import com.specification.service.service.ollama.provider.HuggingFaceAvailabilityChecker;
import com.specification.service.service.ollama.provider.HuggingFaceUtility;
import com.specification.service.service.ollama.provider.OllamaAvailabilityChecker;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Unified LLM service: uses Ollama when available, otherwise HuggingFace.
 * On runtime Ollama failure, falls back to HuggingFace when configured.
 */
@Slf4j
@Service
public class LangChain4jOllamaService {

    private final OllamaProperties properties;
    private final AiBulkheadRegistry bulkheadRegistry;
    private final AiBulkheadSupport bulkheadSupport;
    private final AiProviderSelector aiProviderSelector;
    private final HuggingFaceUtility huggingFaceUtility;
    private final OllamaAvailabilityChecker ollamaAvailabilityChecker;
    private final HuggingFaceAvailabilityChecker huggingFaceAvailabilityChecker;
    private final ConcurrentHashMap<String, ChatModel> modelCache = new ConcurrentHashMap<>();

    public LangChain4jOllamaService(
            OllamaProperties properties,
            AiBulkheadRegistry bulkheadRegistry,
            AiBulkheadSupport bulkheadSupport,
            AiProviderSelector aiProviderSelector,
            HuggingFaceUtility huggingFaceUtility,
            OllamaAvailabilityChecker ollamaAvailabilityChecker,
            HuggingFaceAvailabilityChecker huggingFaceAvailabilityChecker) {
        this.properties = properties;
        this.bulkheadRegistry = bulkheadRegistry;
        this.bulkheadSupport = bulkheadSupport;
        this.aiProviderSelector = aiProviderSelector;
        this.huggingFaceUtility = huggingFaceUtility;
        this.ollamaAvailabilityChecker = ollamaAvailabilityChecker;
        this.huggingFaceAvailabilityChecker = huggingFaceAvailabilityChecker;
    }

    public Mono<String> chatAsync(String userMessage, String model, AiAgentType agentType) {
        return bulkheadSupport.toMono(submitChat(() -> chat(userMessage, model, agentType)));
    }

    public String chat(String userMessage, String model, AiAgentType agentType) {
        AiProviderType provider = aiProviderSelector.selectProvider();
        try {
            return chatWithProvider(userMessage, model, agentType, provider);
        } catch (Exception ex) {
            return fallbackFromOllama(ex, provider, () -> chatWithProvider(userMessage, model, agentType, AiProviderType.HUGGING_FACE));
        }
    }

    public String generateRaw(String prompt, String model) {
        AiProviderType provider = aiProviderSelector.selectProvider();
        try {
            return generateWithProvider(prompt, model, provider);
        } catch (Exception ex) {
            return fallbackFromOllama(ex, provider, () -> generateWithProvider(prompt, model, AiProviderType.HUGGING_FACE));
        }
    }

    public Mono<String> generateRawAsync(String prompt, String model) {
        return bulkheadSupport.toMono(submitAgent(() -> generateRaw(prompt, model)));
    }

    public CompletableFuture<String> generateRawFuture(String prompt, String model) {
        return submitAgent(() -> generateRaw(prompt, model));
    }

    public AiProviderType getActiveProvider() {
        return aiProviderSelector.selectProvider();
    }

    public String getDefaultModel() {
        AiProviderType provider = aiProviderSelector.selectProvider();
        return provider == AiProviderType.OLLAMA
                ? properties.getDefaultModel()
                : huggingFaceUtility.getDefaultModel();
    }

    public String getBaseUrl() {
        AiProviderType provider = aiProviderSelector.selectProvider();
        return provider == AiProviderType.OLLAMA
                ? properties.getBaseUrl()
                : huggingFaceUtility.getBaseUrlForDisplay();
    }

    private String chatWithProvider(String userMessage, String model, AiAgentType agentType, AiProviderType provider) {
        ChatModel chatModel = resolveModel(model, provider);
        AiAgentType resolved = agentType == null ? AiAgentType.AUTO : agentType;

        log.info("AI chat using provider={} model={}", provider, resolveModelName(model, provider));

        return switch (resolved) {
            case SPEC_WRITER -> specWriter(chatModel).write(userMessage);
            case CHAT, AUTO -> chatAssistant(chatModel).chat(userMessage);
            case CODE_AGENT -> chatModel.chat(userMessage);
        };
    }

    private String generateWithProvider(String prompt, String model, AiProviderType provider) {
        log.info("AI agent LLM using provider={} model={}", provider, resolveModelName(model, provider));
        return resolveModel(model, provider).chat(prompt);
    }

    private String fallbackFromOllama(Exception ex, AiProviderType attempted, java.util.function.Supplier<String> hfCall) {
        if (attempted != AiProviderType.OLLAMA || !huggingFaceAvailabilityChecker.isAvailable()) {
            throw ex instanceof RuntimeException runtime ? runtime : new RuntimeException(ex);
        }
        ollamaAvailabilityChecker.invalidateCache();
        log.warn("Ollama request failed, falling back to HuggingFace: {}", ex.getMessage());
        return hfCall.get();
    }

    private CompletableFuture<String> submitChat(java.util.function.Supplier<String> task) {
        return bulkheadRegistry.get(AiBulkheadType.CHAT).submit(task);
    }

    private CompletableFuture<String> submitAgent(java.util.function.Supplier<String> task) {
        return bulkheadRegistry.get(AiBulkheadType.AGENT).submit(task);
    }

    private ChatModel resolveModel(String model, AiProviderType provider) {
        String resolved = resolveModelName(model, provider);
        String cacheKey = provider.name() + ":" + resolved;
        return modelCache.computeIfAbsent(cacheKey, key -> buildModel(provider, resolved));
    }

    private ChatModel buildModel(AiProviderType provider, String modelName) {
        return switch (provider) {
            case OLLAMA -> OllamaChatModel.builder()
                    .baseUrl(properties.getBaseUrl())
                    .modelName(modelName)
                    .timeout(properties.timeout())
                    .temperature(0.7)
                    .build();
            case HUGGING_FACE -> huggingFaceUtility.buildChatModel(modelName);
        };
    }

    private String resolveModelName(String model, AiProviderType provider) {
        if (StringUtils.hasText(model) && !"auto".equalsIgnoreCase(model.trim())) {
            String name = model.trim();
            if (provider == AiProviderType.HUGGING_FACE) {
                return huggingFaceUtility.resolveModelName(name);
            }
            return name;
        }
        return provider == AiProviderType.OLLAMA
                ? properties.getDefaultModel()
                : huggingFaceUtility.getDefaultModel();
    }

    private SpecificationChatAssistant chatAssistant(ChatModel model) {
        return AiServices.builder(SpecificationChatAssistant.class)
                .chatModel(model)
                .build();
    }

    private SpecificationSpecWriterAssistant specWriter(ChatModel model) {
        return AiServices.builder(SpecificationSpecWriterAssistant.class)
                .chatModel(model)
                .build();
    }
}
