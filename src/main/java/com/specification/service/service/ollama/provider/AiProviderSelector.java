package com.specification.service.service.ollama.provider;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.exception.ErrorConstant;
import com.specification.service.response.apires.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AiProviderSelector {

    private final OllamaAvailabilityChecker ollamaAvailabilityChecker;
    private final HuggingFaceUtility huggingFaceUtility;
    private final HuggingFaceAvailabilityChecker huggingFaceAvailabilityChecker;

    public AiProviderSelector(
            OllamaAvailabilityChecker ollamaAvailabilityChecker,
            HuggingFaceUtility huggingFaceUtility,
            HuggingFaceAvailabilityChecker huggingFaceAvailabilityChecker) {
        this.ollamaAvailabilityChecker = ollamaAvailabilityChecker;
        this.huggingFaceUtility = huggingFaceUtility;
        this.huggingFaceAvailabilityChecker = huggingFaceAvailabilityChecker;
    }

    public AiProviderType selectProvider() {
        if (ollamaAvailabilityChecker.isAvailable()) {
            log.debug("AI provider selected: OLLAMA");
            return AiProviderType.OLLAMA;
        }
        if (huggingFaceUtility.isConfigured() && huggingFaceAvailabilityChecker.isAvailable()) {
            log.debug("AI provider selected: HUGGING_FACE (Ollama unavailable)");
            return AiProviderType.HUGGING_FACE;
        }
        throw new ServiceException(
                ApplicationConstant.ERROR_CODE_INTERNAL,
                "Ollama is not running and HuggingFace is unreachable. "
                        + "Start Ollama locally (ollama serve) or check network access to router.huggingface.co.",
                ErrorConstant.CATEGORY.TH,
                ErrorConstant.SEVERITY.I,
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }
}
