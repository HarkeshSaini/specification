package com.specification.service.controller.ollama;

import com.specification.service.response.apires.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.util.List;

@RequestMapping(path = "/api/website")
public interface IAiModelsController {

    @GetMapping(value = "/ai-models")
    Mono<ResponseEntity<APIResponse<List<String>>>> listModels();
}
