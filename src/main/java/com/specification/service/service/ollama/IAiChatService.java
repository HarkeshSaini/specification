package com.specification.service.service.ollama;

import com.specification.service.request.agent.AgentUserRequest;
import reactor.core.publisher.Mono;

public interface IAiChatService {

    Mono<String> chatAIRequest(AgentUserRequest userRequest);
}
