package com.specification.service.controller.ollama;

import com.specification.service.request.agent.AgentUserRequest;
import com.specification.service.response.agent.AgentUserResponse;
import com.specification.service.response.apires.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@RequestMapping(path = "/api/website")
public interface IAiChatController {

    @GetMapping(value = "/ai-chat")
    Mono<ResponseEntity<APIResponse<AgentUserResponse>>> chatAIRequest(@RequestBody AgentUserRequest userRequest);

    @PostMapping(value = "/ai-chat")
    Mono<ResponseEntity<APIResponse<AgentUserResponse>>> chatAIRequestPost(@RequestBody AgentUserRequest userRequest);
}
