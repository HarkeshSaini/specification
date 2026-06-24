package com.specification.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Data
@Component
@ConfigurationProperties(prefix = "ollama")
public class OllamaProperties {

    private String baseUrl = "http://localhost:11434";
    private String defaultModel = "llama3";
    private int timeoutMinutes = 5;

    public Duration timeout() {
        return Duration.ofMinutes(Math.max(1, timeoutMinutes));
    }
}
