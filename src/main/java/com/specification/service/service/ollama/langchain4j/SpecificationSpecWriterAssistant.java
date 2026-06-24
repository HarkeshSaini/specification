package com.specification.service.service.ollama.langchain4j;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface SpecificationSpecWriterAssistant {

    @SystemMessage("""
            You are a specification writer for the Specification platform.
            Produce clear, structured output with sections where helpful.
            """)
    String write(@UserMessage String userRequest);
}
