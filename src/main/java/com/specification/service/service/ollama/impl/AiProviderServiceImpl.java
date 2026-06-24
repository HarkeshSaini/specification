package com.specification.service.service.ollama.impl;

import com.specification.service.response.agent.AiProviderStatusResponse;
import com.specification.service.service.ollama.IAiProviderService;
import com.specification.service.service.ollama.langchain4j.LangChain4jOllamaService;
import com.specification.service.service.ollama.provider.AiProviderType;
import com.specification.service.service.ollama.provider.HuggingFaceAvailabilityChecker;
import com.specification.service.service.ollama.provider.HuggingFaceUtility;
import com.specification.service.service.ollama.provider.OllamaAvailabilityChecker;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class AiProviderServiceImpl implements IAiProviderService {

    private final OllamaAvailabilityChecker ollamaAvailabilityChecker;
    private final HuggingFaceUtility huggingFaceUtility;
    private final HuggingFaceAvailabilityChecker huggingFaceAvailabilityChecker;
    private final LangChain4jOllamaService langChain4jOllamaService;

    public AiProviderServiceImpl(
            OllamaAvailabilityChecker ollamaAvailabilityChecker,
            HuggingFaceUtility huggingFaceUtility,
            HuggingFaceAvailabilityChecker huggingFaceAvailabilityChecker,
            LangChain4jOllamaService langChain4jOllamaService) {
        this.ollamaAvailabilityChecker = ollamaAvailabilityChecker;
        this.huggingFaceUtility = huggingFaceUtility;
        this.huggingFaceAvailabilityChecker = huggingFaceAvailabilityChecker;
        this.langChain4jOllamaService = langChain4jOllamaService;
    }

    @Override
    public Mono<AiProviderStatusResponse> getStatus() {
        return Mono.fromCallable(this::buildStatus).subscribeOn(Schedulers.boundedElastic());
    }

    private AiProviderStatusResponse buildStatus() {
        boolean ollamaUp = ollamaAvailabilityChecker.isAvailable();
        boolean hfConfigured = huggingFaceUtility.isConfigured();
        boolean hfAvailable = huggingFaceAvailabilityChecker.isAvailable();

        AiProviderType active = resolveActiveProvider(ollamaUp, hfAvailable);

        return AiProviderStatusResponse.builder()
                .activeProvider(active != null ? active.name() : "NONE")
                .ollamaAvailable(ollamaUp)
                .huggingFaceConfigured(hfConfigured)
                .huggingFaceAvailable(hfAvailable)
                .huggingFaceFreeMode(huggingFaceUtility.isFreeMode())
                .defaultModel(active != null ? langChain4jOllamaService.getDefaultModel() : null)
                .baseUrl(active != null ? langChain4jOllamaService.getBaseUrl() : null)
                .build();
    }

    private AiProviderType resolveActiveProvider(boolean ollamaUp, boolean hfAvailable) {
        if (ollamaUp) {
            return AiProviderType.OLLAMA;
        }
        if (hfAvailable) {
            return AiProviderType.HUGGING_FACE;
        }
        return null;
    }
}
