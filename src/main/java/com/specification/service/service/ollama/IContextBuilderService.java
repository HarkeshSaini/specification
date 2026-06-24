package com.specification.service.service.ollama;

import com.specification.service.request.agent.AgentUserRequest;
import com.specification.service.response.agent.AgentContext;
import com.specification.service.response.agent.AgentScanResult;

public interface IContextBuilderService {
    AgentContext build(AgentUserRequest request, AgentScanResult scanResult);
}
