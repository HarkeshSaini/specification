package com.specification.service.controller.ollama.impl;

import com.specification.service.controller.ollama.IAiProviderController;
import com.specification.service.response.agent.AiProviderStatusResponse;
import com.specification.service.response.apires.APIResponse;
import com.specification.service.service.ollama.IAiProviderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AiProviderControllerImpl implements IAiProviderController {

    private final IAiProviderService aiProviderService;

    public AiProviderControllerImpl(IAiProviderService aiProviderService) {
        this.aiProviderService = aiProviderService;
    }

    @Override
    public Mono<ResponseEntity<APIResponse<AiProviderStatusResponse>>> getProviderStatus() {
        return aiProviderService.getStatus()
                .map(status -> {
                    APIResponse<AiProviderStatusResponse> body = APIResponse.successResponse(
                            status,
                            "AI provider status",
                            "OK"
                    ).getBody();
                    return ResponseEntity.ok(body);
                });
    }
}
