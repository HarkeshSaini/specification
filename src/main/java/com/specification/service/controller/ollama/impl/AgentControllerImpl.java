package com.specification.service.controller.ollama.impl;

import com.specification.service.controller.ollama.IAgentController;
import com.specification.service.request.agent.AgentBatchRequest;
import com.specification.service.request.agent.AgentUserRequest;
import com.specification.service.response.agent.AgentBatchResponse;
import com.specification.service.response.agent.AgentUserResponse;
import com.specification.service.response.apires.APIResponse;
import com.specification.service.service.ollama.IAgentAsyncService;
import com.specification.service.service.ollama.bulkhead.AiBulkheadSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Validated
@RestController
public class AgentControllerImpl implements IAgentController {

    private final IAgentAsyncService agentAsyncService;
    private final AiBulkheadSupport bulkheadSupport;

    public AgentControllerImpl(IAgentAsyncService agentAsyncService, AiBulkheadSupport bulkheadSupport) {
        this.agentAsyncService = agentAsyncService;
        this.bulkheadSupport = bulkheadSupport;
    }

    @Override
    public Mono<ResponseEntity<APIResponse<AgentUserResponse>>> agentAIRequest(AgentUserRequest userRequest) {
        return buildAgentResponse(userRequest);
    }

    @Override
    public Mono<ResponseEntity<APIResponse<AgentUserResponse>>> agentAIRequestPost(AgentUserRequest userRequest) {
        return buildAgentResponse(userRequest);
    }

    @Override
    public Mono<ResponseEntity<APIResponse<AgentBatchResponse>>> agentAIBatchRequestPost(AgentBatchRequest batchRequest) {
        return bulkheadSupport.toMono(agentAsyncService.executeBatchAsync(batchRequest))
                .map(body -> {
                    APIResponse<AgentBatchResponse> response = APIResponse.successResponse(body, "Batch agent processing complete", HttpStatus.OK.getReasonPhrase()).getBody();
                    return ResponseEntity.ok(response);
                });
    }

    private Mono<ResponseEntity<APIResponse<AgentUserResponse>>> buildAgentResponse(AgentUserRequest userRequest) {
        return bulkheadSupport.toMono(agentAsyncService.executeAsync(userRequest))
                .map(agentUserResponse -> {
                    APIResponse<AgentUserResponse> response = APIResponse.successResponse(agentUserResponse, "Successfully Created Response: ", HttpStatus.CREATED.getReasonPhrase()).getBody();
                    return ResponseEntity.ok(response);
                });
    }
}
