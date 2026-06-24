package com.specification.service.controller.ollama.impl;

import com.specification.service.controller.ollama.IAiChatController;
import com.specification.service.facade.ollama.IAiChatFacade;
import com.specification.service.request.agent.AgentUserRequest;
import com.specification.service.response.agent.AgentUserResponse;
import com.specification.service.response.apires.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Validated
@RestController
public class AiChatControllerImpl implements IAiChatController {

    private final IAiChatFacade iAiChatServiceFacade;

    public AiChatControllerImpl(IAiChatFacade iAiChatServiceFacade) {
        this.iAiChatServiceFacade = iAiChatServiceFacade;
    }

    @Override
    public Mono<ResponseEntity<APIResponse<AgentUserResponse>>> chatAIRequest(AgentUserRequest userRequest) {
        return buildChatResponse(userRequest);
    }

    @Override
    public Mono<ResponseEntity<APIResponse<AgentUserResponse>>> chatAIRequestPost(AgentUserRequest userRequest) {
        return buildChatResponse(userRequest);
    }

    private Mono<ResponseEntity<APIResponse<AgentUserResponse>>> buildChatResponse(AgentUserRequest userRequest) {
        return iAiChatServiceFacade.chatAIRequest(userRequest)
                .map(agentUserResponse -> {
                    APIResponse<AgentUserResponse> response = APIResponse.successResponse(agentUserResponse,"Successfully Created Response: ", HttpStatus.CREATED.getReasonPhrase()).getBody();
                    return ResponseEntity.ok(response);
                });
    }
}
