package com.specification.service.service.ollama;

import com.specification.service.request.agent.AgentBatchRequest;
import com.specification.service.request.agent.AgentUserRequest;
import com.specification.service.response.agent.AgentBatchResponse;
import com.specification.service.response.agent.AgentUserResponse;

import java.util.concurrent.CompletableFuture;

public interface IAgentAsyncService {

    CompletableFuture<AgentUserResponse> executeAsync(AgentUserRequest request);

    CompletableFuture<AgentBatchResponse> executeBatchAsync(AgentBatchRequest request);
}
