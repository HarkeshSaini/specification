package com.specification.service.response.agent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiProviderStatusResponse {

    private String activeProvider;
    private boolean ollamaAvailable;
    private boolean huggingFaceConfigured;
    /** True when using HuggingFace without an API key (free hf-inference tier). */
    private boolean huggingFaceFreeMode;
    /** True when HuggingFace router/API is reachable from this server. */
    private boolean huggingFaceAvailable;
    private String defaultModel;
    private String baseUrl;
}
