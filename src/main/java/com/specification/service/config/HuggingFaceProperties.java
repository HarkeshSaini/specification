package com.specification.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;

@Data
@Component
@ConfigurationProperties(prefix = "huggingface")
public class HuggingFaceProperties {

    private boolean enabled = true;
    private String apiKey = "";
    /** Full router — requires free HF token from huggingface.co/settings/tokens */
    private String baseUrl = "https://router.huggingface.co/v1";
    /** Free CPU inference — works without API key */
    private String freeBaseUrl = "https://router.huggingface.co/hf-inference/v1";
    /** Used when HF_API_KEY is set */
    private String defaultModel = "meta-llama/Meta-Llama-3-8B-Instruct";
    /** Used in free mode when no API key */
    private String freeDefaultModel = "HuggingFaceTB/SmolLM3-3B:hf-inference";
    private int timeoutSeconds = 120;

    public Duration timeout() {
        return Duration.ofSeconds(Math.max(10, timeoutSeconds));
    }

    public String getDefaultModel() {
        return StringUtils.hasText(apiKey) ? defaultModel : freeDefaultModel;
    }
}
