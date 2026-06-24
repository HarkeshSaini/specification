package com.specification.service.request.agent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentUserRequest {

    private String model;
    private String command;
    private String filePath;
    private String agent;
    private String auto;

}
