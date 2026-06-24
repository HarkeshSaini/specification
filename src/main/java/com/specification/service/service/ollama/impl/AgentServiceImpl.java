package com.specification.service.service.ollama.impl;

import com.specification.service.request.agent.AgentUserRequest;
import com.specification.service.response.agent.AgentContext;
import com.specification.service.response.agent.AgentScanResult;
import com.specification.service.service.ollama.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AgentServiceImpl implements IAgentService {

    private final ICodeScannerService scanner;
    private final IContextBuilderService contextBuilder;
    private final IOllamaClientService llm;
    private final IPlanParserService parser;
    private final ICodeModifierService modifier;

    public AgentServiceImpl(ICodeScannerService scanner,IContextBuilderService contextBuilder, IOllamaClientService llm, IPlanParserService parser, ICodeModifierService modifier) {
        this.scanner = scanner;
        this.contextBuilder = contextBuilder;
        this.llm = llm;
        this.parser = parser;
        this.modifier = modifier;
    }

    @Override
    public String agentAIRequest(AgentUserRequest request) {
        if (request == null || !StringUtils.hasText(request.getFilePath())) {
            throw new IllegalArgumentException("filePath is required");
        }
        if (!StringUtils.hasText(request.getCommand())) {
            throw new IllegalArgumentException("command is required");
        }

        AgentScanResult scan = scanner.scan(request.getFilePath());
        AgentContext context = contextBuilder.build(request, scan);
        String llmResponse = llm.callLLM(context.getEnrichedPrompt(), request.getModel());
        ChangePlanServiceImpl plan = parser.parse(llmResponse);
        modifier.apply(request.getFilePath(), scan.getCode(), plan);

        int changeCount = plan.getChanges() != null ? plan.getChanges().size() : 0;
        return "Code modification complete (" + changeCount + " change(s) applied)";
    }
}
