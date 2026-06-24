package com.specification.service.service.ollama;

import com.specification.service.response.agent.AiProviderStatusResponse;
import reactor.core.publisher.Mono;

public interface IAiProviderService {

    Mono<AiProviderStatusResponse> getStatus();
}
