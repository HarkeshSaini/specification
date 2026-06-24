package com.specification.service.service.ollama.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.specification.service.config.OllamaProperties;
import com.specification.service.service.ollama.IOllamaModelService;
import com.specification.service.service.ollama.bulkhead.AiBulkheadRegistry;
import com.specification.service.service.ollama.bulkhead.AiBulkheadSupport;
import com.specification.service.service.ollama.bulkhead.AiBulkheadType;
import com.specification.service.service.ollama.provider.HuggingFaceAvailabilityChecker;
import com.specification.service.service.ollama.provider.HuggingFaceUtility;
import com.specification.service.service.ollama.provider.OllamaAvailabilityChecker;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OllamaModelServiceImpl implements IOllamaModelService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final OllamaProperties properties;
    private final AiBulkheadRegistry bulkheadRegistry;
    private final AiBulkheadSupport bulkheadSupport;
    private final OllamaAvailabilityChecker ollamaAvailabilityChecker;
    private final HuggingFaceUtility huggingFaceUtility;
    private final HuggingFaceAvailabilityChecker huggingFaceAvailabilityChecker;

    public OllamaModelServiceImpl(
            WebClient.Builder webClientBuilder,
            ObjectMapper objectMapper,
            OllamaProperties properties,
            AiBulkheadRegistry bulkheadRegistry,
            AiBulkheadSupport bulkheadSupport,
            OllamaAvailabilityChecker ollamaAvailabilityChecker,
            HuggingFaceUtility huggingFaceUtility,
            HuggingFaceAvailabilityChecker huggingFaceAvailabilityChecker) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
        this.properties = properties;
        this.bulkheadRegistry = bulkheadRegistry;
        this.bulkheadSupport = bulkheadSupport;
        this.ollamaAvailabilityChecker = ollamaAvailabilityChecker;
        this.huggingFaceUtility = huggingFaceUtility;
        this.huggingFaceAvailabilityChecker = huggingFaceAvailabilityChecker;
    }

    @Override
    public Mono<List<String>> listAvailableModels() {
        return bulkheadSupport.toMono(
                bulkheadRegistry.get(AiBulkheadType.MODELS).submit(this::fetchModelsBlocking)
        ).onErrorReturn(fallbackModels());
    }

    private List<String> fetchModelsBlocking() {
        if (ollamaAvailabilityChecker.isAvailable()) {
            try {
                String raw = webClient.get()
                        .uri(properties.getBaseUrl() + "/api/tags")
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
                List<String> ollamaModels = parseOllamaModelNames(raw);
                if (!ollamaModels.isEmpty()) {
                    return ollamaModels;
                }
            } catch (Exception ignored) {
                /* fall through to HuggingFace */
            }
        }
        if (huggingFaceUtility.isConfigured() && huggingFaceAvailabilityChecker.isAvailable()) {
            return huggingFaceUtility.listSuggestedModels();
        }
        return fallbackModels();
    }

    private List<String> parseOllamaModelNames(String raw) {
        try {
            JsonNode root = objectMapper.readTree(raw);
            JsonNode models = root.get("models");
            if (models == null || !models.isArray()) {
                return Collections.emptyList();
            }
            List<String> names = new ArrayList<>();
            for (JsonNode model : models) {
                if (model.has("name")) {
                    names.add(model.get("name").asText());
                }
            }
            return names;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<String> fallbackModels() {
        if (huggingFaceUtility.isConfigured() && huggingFaceAvailabilityChecker.isAvailable()) {
            return huggingFaceUtility.listSuggestedModels();
        }
        return Collections.singletonList(properties.getDefaultModel());
    }
}
