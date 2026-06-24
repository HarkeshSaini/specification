package com.specification.service.service.ollama.langchain4j;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface SpecificationChatAssistant {

    @SystemMessage("""
            You are Specification AI, a helpful assistant for the Specification platform.
            Answer questions about specs, APIs, authentication, and software design clearly and concisely.
            """)
    String chat(@UserMessage String userMessage);
}
