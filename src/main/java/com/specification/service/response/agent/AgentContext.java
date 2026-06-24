package com.specification.service.response.agent;

import lombok.Data;

@Data
public class AgentContext {

    private String enrichedPrompt;

    public AgentContext(String enrichedPrompt) {
        this.enrichedPrompt = enrichedPrompt;
    }

}
