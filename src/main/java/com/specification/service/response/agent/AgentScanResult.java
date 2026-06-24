package com.specification.service.response.agent;

import lombok.Data;

@Data
public class AgentScanResult {

    private String code;

    public AgentScanResult(String code) {
        this.code = code;
    }

}

