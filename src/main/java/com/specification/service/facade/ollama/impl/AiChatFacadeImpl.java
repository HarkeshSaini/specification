package com.specification.service.facade.ollama.impl;

import com.specification.service.facade.ollama.IAiChatFacade;
import com.specification.service.request.agent.AgentUserRequest;
import com.specification.service.response.agent.AgentUserResponse;
import com.specification.service.service.ollama.IAiChatService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AiChatFacadeImpl implements IAiChatFacade {

    private final IAiChatService iAiChatService;

    public AiChatFacadeImpl(IAiChatService iAiChatService) {
        this.iAiChatService = iAiChatService;
    }

    @Override
    public Mono<AgentUserResponse> chatAIRequest(AgentUserRequest userRequest) {
        return iAiChatService.chatAIRequest(userRequest).map(AgentUserResponse::new);
    }
}
