package com.specification.service.facade.ollama;

import com.specification.service.request.agent.AgentUserRequest;
import com.specification.service.response.agent.AgentUserResponse;
import reactor.core.publisher.Mono;

public interface IAiChatFacade {

    Mono<AgentUserResponse> chatAIRequest(AgentUserRequest userRequest);
}
