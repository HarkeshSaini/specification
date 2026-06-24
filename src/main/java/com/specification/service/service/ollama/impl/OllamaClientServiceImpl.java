package com.specification.service.service.ollama.impl;

import com.specification.service.service.ollama.IOllamaClientService;
import com.specification.service.service.ollama.langchain4j.LangChain4jOllamaService;
import org.springframework.stereotype.Service;

@Service
public class OllamaClientServiceImpl implements IOllamaClientService {

    private final LangChain4jOllamaService langChain4jOllamaService;

    public OllamaClientServiceImpl(LangChain4jOllamaService langChain4jOllamaService) {
        this.langChain4jOllamaService = langChain4jOllamaService;
    }

    @Override
    public String callLLM(String prompt, String model) {
        return langChain4jOllamaService.generateRaw(prompt, model);
    }
}
