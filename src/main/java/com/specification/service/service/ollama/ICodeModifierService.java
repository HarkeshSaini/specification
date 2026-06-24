package com.specification.service.service.ollama;

import com.specification.service.service.ollama.impl.ChangePlanServiceImpl;

public interface ICodeModifierService {
    void apply(String filePath, String originalCode, ChangePlanServiceImpl plan);
}
