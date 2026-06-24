package com.specification.service.service.ollama.impl;

import com.specification.service.request.agent.AgentUserRequest;
import com.specification.service.service.ollama.AiAgentType;
import com.specification.service.service.ollama.IAiChatService;
import com.specification.service.service.ollama.langchain4j.LangChain4jOllamaService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Service
public class IAiChatServiceImpl implements IAiChatService {

    private final LangChain4jOllamaService langChain4jOllamaService;

    public IAiChatServiceImpl(LangChain4jOllamaService langChain4jOllamaService) {
        this.langChain4jOllamaService = langChain4jOllamaService;
    }

    @Override
    public Mono<String> chatAIRequest(AgentUserRequest userRequest) {
        if (userRequest == null || !StringUtils.hasText(userRequest.getCommand())) {
            return Mono.error(new IllegalArgumentException("command is required"));
        }
        AiAgentType agentType = AiAgentType.fromRequest(userRequest.getAgent(), userRequest.getAuto());
        return langChain4jOllamaService.chatAsync(
                userRequest.getCommand(),
                userRequest.getModel(),
                agentType
        );
    }
}
