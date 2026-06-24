package com.specification.service.service.ollama;

import reactor.core.publisher.Mono;

import java.util.List;

public interface IOllamaModelService {

    Mono<List<String>> listAvailableModels();
}
