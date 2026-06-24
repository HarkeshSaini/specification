package com.specification.service.service.ollama.impl;

import com.specification.service.facade.ollama.IAgentFacade;
import com.specification.service.request.agent.AgentBatchRequest;
import com.specification.service.request.agent.AgentUserRequest;
import com.specification.service.response.agent.AgentBatchResponse;
import com.specification.service.response.agent.AgentBatchResult;
import com.specification.service.response.agent.AgentUserResponse;
import com.specification.service.service.ollama.IAgentAsyncService;
import com.specification.service.service.ollama.bulkhead.AiBulkheadRegistry;
import com.specification.service.service.ollama.bulkhead.AiBulkheadType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AgentAsyncServiceImpl implements IAgentAsyncService {

    private final IAgentFacade agentFacade;
    private final AiBulkheadRegistry bulkheadRegistry;

    public AgentAsyncServiceImpl(IAgentFacade agentFacade, AiBulkheadRegistry bulkheadRegistry) {
        this.agentFacade = agentFacade;
        this.bulkheadRegistry = bulkheadRegistry;
    }

    @Override
    public CompletableFuture<AgentUserResponse> executeAsync(AgentUserRequest request) {
        return bulkheadRegistry.get(AiBulkheadType.AGENT).submit(() -> agentFacade.agentAIRequest(request));
    }

    @Override
    public CompletableFuture<AgentBatchResponse> executeBatchAsync(AgentBatchRequest request) {
        validateBatch(request);
        List<CompletableFuture<AgentBatchResult>> futures = request.getFilePaths().stream().map(path -> bulkheadRegistry.get(AiBulkheadType.AGENT).submit(() -> runOne(request, path))).toList();
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).thenApply(ignored -> aggregate(futures));
    }

    private AgentBatchResult runOne(AgentBatchRequest batch, String filePath) {
        try {
            AgentUserRequest single = AgentUserRequest.builder().command(batch.getCommand()).model(batch.getModel()).filePath(filePath).agent(batch.getAgent()).auto(batch.getAuto()).build();
            AgentUserResponse response = agentFacade.agentAIRequest(single);
            return AgentBatchResult.builder().filePath(filePath).response(response.getResponse()).success(true).build();
        } catch (Exception ex) {
            return AgentBatchResult.builder().filePath(filePath).success(false).error(ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName()).build();
        }
    }

    private static AgentBatchResponse aggregate(List<CompletableFuture<AgentBatchResult>> futures) {
        List<AgentBatchResult> results = new ArrayList<>(futures.size());
        int success = 0;
        int failure = 0;
        for (CompletableFuture<AgentBatchResult> future : futures) {
            AgentBatchResult result = future.join();
            results.add(result);
            if (result.isSuccess()) {
                success++;
            } else {
                failure++;
            }
        }
        return AgentBatchResponse.builder().results(results).successCount(success).failureCount(failure).build();
    }

    private static void validateBatch(AgentBatchRequest request) {
        if (request == null || request.getFilePaths() == null || request.getFilePaths().isEmpty()) {
            throw new IllegalArgumentException("filePaths is required");
        }
        if (!StringUtils.hasText(request.getCommand())) {
            throw new IllegalArgumentException("command is required");
        }
    }
}
