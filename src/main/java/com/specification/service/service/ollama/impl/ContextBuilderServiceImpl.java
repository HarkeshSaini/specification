package com.specification.service.service.ollama.impl;

import com.specification.service.request.agent.AgentUserRequest;
import com.specification.service.response.agent.AgentContext;
import com.specification.service.response.agent.AgentScanResult;
import com.specification.service.service.ollama.IContextBuilderService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ContextBuilderServiceImpl implements IContextBuilderService {

    private static final String PLAN_SCHEMA = """
            {
              "changes": [
                {"type": "replace", "target": "exact text to find", "content": "replacement text"},
                {"type": "insert", "target": "anchor text", "content": "text to insert after anchor"},
                {"type": "delete", "target": "exact text to remove", "content": ""},
                {"type": "overwrite", "target": "", "content": "full new file content"}
              ]
            }
            """;

    @Override
    public AgentContext build(AgentUserRequest request, AgentScanResult scanResult) {
        String command = request != null && StringUtils.hasText(request.getCommand())
                ? request.getCommand()
                : "Apply safe refactoring";

        String prompt = """
                You are a code editing agent.
                Return ONLY valid JSON (no markdown, no explanation) matching this schema:
                %s

                Rules:
                - Use "replace" for in-place substitutions; target must match source exactly.
                - Use "insert" to add content immediately after the first occurrence of target.
                - Use "delete" to remove target text.
                - Use "overwrite" only when replacing the entire file.
                - Prefer the smallest number of changes that satisfy the request.

                User request:
                %s

                File content:
                %s
                """.formatted(PLAN_SCHEMA, command, scanResult.getCode());

        return new AgentContext(prompt);
    }
}
