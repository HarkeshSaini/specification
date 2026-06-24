package com.specification.service.controller.ollama.impl;

import com.specification.service.controller.ollama.IAiModelsController;
import com.specification.service.response.apires.APIResponse;
import com.specification.service.service.ollama.IOllamaModelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class AiModelsControllerImpl implements IAiModelsController {

    private final IOllamaModelService ollamaModelService;

    public AiModelsControllerImpl(IOllamaModelService ollamaModelService) {
        this.ollamaModelService = ollamaModelService;
    }

    @Override
    public Mono<ResponseEntity<APIResponse<List<String>>>> listModels() {
        return ollamaModelService.listAvailableModels()
                .map(models -> {
                    APIResponse<List<String>> response = APIResponse.successResponse(
                            models,
                            "Available AI models (Ollama or HuggingFace fallback)",
                            "OK"
                    ).getBody();
                    return ResponseEntity.ok(response);
                });
    }
}
