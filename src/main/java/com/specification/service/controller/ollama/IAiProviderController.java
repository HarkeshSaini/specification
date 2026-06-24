package com.specification.service.controller.ollama;

import com.specification.service.response.agent.AiProviderStatusResponse;
import com.specification.service.response.apires.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@RequestMapping(path = "/api/website")
public interface IAiProviderController {

    @GetMapping(value = "/ai-provider")
    Mono<ResponseEntity<APIResponse<AiProviderStatusResponse>>> getProviderStatus();
}
