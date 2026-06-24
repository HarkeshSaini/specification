package com.specification.service.response.agent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentBatchResponse {

    private List<AgentBatchResult> results;
    private int successCount;
    private int failureCount;
}
