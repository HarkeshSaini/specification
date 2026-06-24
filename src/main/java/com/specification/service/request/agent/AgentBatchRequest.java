package com.specification.service.request.agent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentBatchRequest {

    private String model;
    private String command;
    private List<String> filePaths;
    private String agent;
    private String auto;
}
