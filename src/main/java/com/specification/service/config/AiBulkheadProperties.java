package com.specification.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Data
@Component
@ConfigurationProperties(prefix = "ai.bulkhead")
public class AiBulkheadProperties {

    private PoolConfig chat = new PoolConfig(6, 24, 30);
    private PoolConfig agent = new PoolConfig(3, 12, 60);
    private PoolConfig models = new PoolConfig(10, 20, 10);
    private int agentMaxParallelFiles = 4;

    @Data
    public static class PoolConfig {
        private int maxConcurrent;
        private int queueCapacity;
        private int acquireTimeoutSeconds;

        public PoolConfig() {
        }

        public PoolConfig(int maxConcurrent, int queueCapacity, int acquireTimeoutSeconds) {
            this.maxConcurrent = maxConcurrent;
            this.queueCapacity = queueCapacity;
            this.acquireTimeoutSeconds = acquireTimeoutSeconds;
        }

        public Duration acquireTimeout() {
            return Duration.ofSeconds(Math.max(1, acquireTimeoutSeconds));
        }
    }
}
