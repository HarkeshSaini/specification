package com.specification.service.service.ollama;

import org.springframework.util.StringUtils;

public enum AiAgentType {
    AUTO,
    CHAT,
    SPEC_WRITER,
    CODE_AGENT;

    public static AiAgentType fromRequest(String agent, String auto) {
        if (!StringUtils.hasText(agent) || "auto".equalsIgnoreCase(agent.trim())) {
            return AUTO;
        }
        return switch (agent.trim().toLowerCase()) {
            case "spec-writer" -> SPEC_WRITER;
            case "code-agent" -> CODE_AGENT;
            case "chat" -> CHAT;
            default -> CHAT;
        };
    }
}
