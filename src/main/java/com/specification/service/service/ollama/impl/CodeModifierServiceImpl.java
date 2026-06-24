package com.specification.service.service.ollama.impl;

import com.specification.service.service.ollama.ICodeModifierService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class CodeModifierServiceImpl implements ICodeModifierService {

    @Override
    public void apply(String filePath, String originalCode, ChangePlanServiceImpl plan) {
        if (!StringUtils.hasText(filePath)) {
            throw new IllegalArgumentException("filePath is required");
        }
        if (originalCode == null) {
            throw new IllegalArgumentException("originalCode is required");
        }
        String modifiedCode = originalCode;
        List<ChangePlanServiceImpl.Change> changes = plan != null ? plan.getChanges() : List.of();

        for (ChangePlanServiceImpl.Change change : changes) {
            if (change == null || !StringUtils.hasText(change.getType())) {
                continue;
            }
            String type = change.getType().trim().toLowerCase();
            String target = change.getTarget() != null ? change.getTarget() : "";
            String content = change.getContent() != null ? change.getContent() : "";

            switch (type) {
                case "replace" -> modifiedCode = modifiedCode.replace(target, content);
                case "insert" -> {
                    int index = modifiedCode.indexOf(target);
                    if (index >= 0) {
                        int insertAt = index + target.length();
                        modifiedCode = modifiedCode.substring(0, insertAt) + content + modifiedCode.substring(insertAt);
                    }
                }
                case "delete" -> modifiedCode = modifiedCode.replace(target, "");
                case "overwrite" -> modifiedCode = content;
                default -> throw new IllegalArgumentException("Unsupported change type: " + type);
            }
        }

        try {
            Path path = Paths.get(filePath).toAbsolutePath().normalize();
            Files.writeString(path, modifiedCode, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write file: " + filePath, e);
        }
    }
}
