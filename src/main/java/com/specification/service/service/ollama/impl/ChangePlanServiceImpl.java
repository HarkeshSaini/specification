package com.specification.service.service.ollama.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.specification.service.service.ollama.IChangePlanService;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangePlanServiceImpl implements IChangePlanService {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Change {
        private String type;
        private String target;
        private String content;
    }

    private List<Change> changes = new ArrayList<>();

    @Override
    public List<Change> getChanges() {
        return changes;
    }
}
