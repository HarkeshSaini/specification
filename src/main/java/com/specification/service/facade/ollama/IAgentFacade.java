package com.specification.service.facade.ollama;

import com.specification.service.request.agent.AgentUserRequest;
import com.specification.service.response.agent.AgentUserResponse;

public interface IAgentFacade {

    AgentUserResponse agentAIRequest(AgentUserRequest userRequest);
}
