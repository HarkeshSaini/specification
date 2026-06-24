package com.specification.service.service.ollama;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OllamaHttpService {

    private static final Pattern JSON_CODE_BLOCK = Pattern.compile("```(?:json)?\\s*([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);

    private final ObjectMapper objectMapper;

    public OllamaHttpService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String extractResponseText(String raw) {
        if (raw == null || raw.isBlank()) {
            return "";
        }
        try {
            JsonNode root = objectMapper.readTree(raw);
            if (root.has("response")) {
                return root.get("response").asText();
            }
        } catch (Exception ignored) {
            // not a single JSON object — may be NDJSON from streaming
        }
        StringBuilder combined = new StringBuilder();
        for (String line : raw.split("\n")) {
            if (line.isBlank()) {
                continue;
            }
            try {
                JsonNode node = objectMapper.readTree(line);
                if (node.has("response")) {
                    combined.append(node.get("response").asText());
                }
            } catch (Exception ignored) {
                combined.append(line);
            }
        }
        return combined.length() > 0 ? combined.toString() : raw;
    }

    public String extractJsonPayload(String text) {
        if (text == null || text.isBlank()) {
            return "{}";
        }
        Matcher matcher = JSON_CODE_BLOCK.matcher(text);
        if (matcher.find()) {
            text = matcher.group(1).trim();
        }
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1);
        }
        return text.trim();
    }
}
