package com.specification.service.service.ollama.impl;

import com.specification.service.response.agent.AgentScanResult;
import com.specification.service.service.ollama.ICodeScannerService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CodeScannerServiceImpl implements ICodeScannerService {

    @Override
    public AgentScanResult scan(String filePath) {
        if (!StringUtils.hasText(filePath)) {
            throw new IllegalArgumentException("filePath is required");
        }
        try {
            Path path = Paths.get(filePath).toAbsolutePath().normalize();
            if (!Files.exists(path)) {
                throw new IllegalArgumentException("File not found: " + path);
            }
            if (!Files.isRegularFile(path)) {
                throw new IllegalArgumentException("Path is not a file: " + path);
            }
            String code = Files.readString(path, StandardCharsets.UTF_8);
            return new AgentScanResult(code);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to scan file: " + filePath, e);
        }
    }
}
