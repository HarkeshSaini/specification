package com.specification.service.service.ollama.bulkhead;

import com.specification.service.config.AiBulkheadProperties;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class AiBulkheadRegistry {

    private final Map<AiBulkheadType, AiBulkheadExecutor> executors;

    public AiBulkheadRegistry(AiBulkheadProperties properties) {
        executors = new EnumMap<>(AiBulkheadType.class);
        executors.put(AiBulkheadType.CHAT, new AiBulkheadExecutor("ai-chat", properties.getChat()));
        executors.put(AiBulkheadType.AGENT, new AiBulkheadExecutor("ai-agent", properties.getAgent()));
        executors.put(AiBulkheadType.MODELS, new AiBulkheadExecutor("ai-models", properties.getModels()));
    }

    public AiBulkheadExecutor get(AiBulkheadType type) {
        return executors.get(type);
    }

    @PreDestroy
    public void shutdown() {
        executors.values().forEach(AiBulkheadExecutor::shutdown);
    }
}
