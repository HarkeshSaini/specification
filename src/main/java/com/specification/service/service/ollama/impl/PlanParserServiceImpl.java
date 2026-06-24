package com.specification.service.service.ollama.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.specification.service.service.ollama.IPlanParserService;
import com.specification.service.service.ollama.OllamaHttpService;
import org.springframework.stereotype.Service;

@Service
public class PlanParserServiceImpl implements IPlanParserService {

    private final ObjectMapper mapper;
    private final OllamaHttpService ollamaHttpService;

    public PlanParserServiceImpl(ObjectMapper mapper, OllamaHttpService ollamaHttpService) {
        this.mapper = mapper;
        this.ollamaHttpService = ollamaHttpService;
    }

    @Override
    public ChangePlanServiceImpl parse(String llmOutput) {
        try {
            String responseText = ollamaHttpService.extractResponseText(llmOutput);
            String planJson = ollamaHttpService.extractJsonPayload(responseText);
            ChangePlanServiceImpl plan = mapper.readValue(planJson, ChangePlanServiceImpl.class);
            if (plan.getChanges() == null) {
                plan.setChanges(java.util.Collections.emptyList());
            }
            return plan;
        } catch (Exception e) {
            throw new RuntimeException("Invalid change plan from LLM: " + e.getMessage(), e);
        }
    }
}
