package com.specification.service.facade.ollama.impl;

import com.specification.service.facade.ollama.IAgentFacade;
import com.specification.service.request.agent.AgentUserRequest;
import com.specification.service.response.agent.AgentUserResponse;
import com.specification.service.service.ollama.IAgentService;
import org.springframework.stereotype.Component;

@Component
public class AgentFacadeImpl implements IAgentFacade {

    private final IAgentService iAgentService;

    public AgentFacadeImpl(IAgentService iAgentService) {
        this.iAgentService = iAgentService;
    }

    @Override
    public AgentUserResponse agentAIRequest(AgentUserRequest userRequest) {
        String mgsResp = this.iAgentService.agentAIRequest(userRequest);
        return new AgentUserResponse(mgsResp);
    }
}
