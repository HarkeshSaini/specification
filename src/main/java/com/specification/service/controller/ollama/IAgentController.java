package com.specification.service.controller.ollama;

import com.specification.service.request.agent.AgentBatchRequest;
import com.specification.service.request.agent.AgentUserRequest;
import com.specification.service.response.agent.AgentBatchResponse;
import com.specification.service.response.agent.AgentUserResponse;
import com.specification.service.response.apires.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@RequestMapping(path = "/api/website")
public interface IAgentController {

    @GetMapping(value = "/ai-agent")
    Mono<ResponseEntity<APIResponse<AgentUserResponse>>> agentAIRequest(@RequestBody AgentUserRequest userRequest);

    @PostMapping(value = "/ai-agent")
    Mono<ResponseEntity<APIResponse<AgentUserResponse>>> agentAIRequestPost(@RequestBody AgentUserRequest userRequest);

    @PostMapping(value = "/ai-agent/batch")
    Mono<ResponseEntity<APIResponse<AgentBatchResponse>>> agentAIBatchRequestPost(@RequestBody AgentBatchRequest batchRequest);
}
