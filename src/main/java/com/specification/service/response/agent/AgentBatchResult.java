package com.specification.service.response.agent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentBatchResult {

    private String filePath;
    private String response;
    private boolean success;
    private String error;
}
