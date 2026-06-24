package com.specification.service.service.ollama;


import com.specification.service.response.agent.AgentScanResult;

public interface ICodeScannerService {

    AgentScanResult scan(String filePath);
}

