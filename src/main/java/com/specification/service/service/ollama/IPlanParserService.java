package com.specification.service.service.ollama;

import com.specification.service.service.ollama.impl.ChangePlanServiceImpl;

public interface IPlanParserService {
    ChangePlanServiceImpl parse(String json);
}
