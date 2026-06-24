package com.specification.service.service.ollama;

public interface IOllamaClientService {
    String callLLM(String prompt, String model);
}
