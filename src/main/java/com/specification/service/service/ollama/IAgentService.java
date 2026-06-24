package com.specification.service.service.ollama;

import com.specification.service.request.agent.AgentUserRequest;

public interface IAgentService {

    String agentAIRequest(AgentUserRequest userRequest);
}
