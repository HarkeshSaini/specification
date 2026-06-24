package com.specification.service.service.ollama.bulkhead;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.exception.ErrorConstant;
import com.specification.service.response.apires.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Component
public class AiBulkheadSupport {

    public <T> Mono<T> toMono(CompletableFuture<T> future) {
        return Mono.fromFuture(future).onErrorMap(this::unwrap);
    }

    public Throwable unwrap(Throwable error) {
        Throwable current = error;
        while (current instanceof CompletionException && current.getCause() != null && current.getCause() != current) {
            current = current.getCause();
        }
        if (current instanceof ServiceException serviceException) {
            return serviceException;
        }
        if (current instanceof IllegalArgumentException) {
            return current;
        }
        String detail = current.getMessage() != null ? current.getMessage() : current.getClass().getSimpleName();
        if (detail.contains("Connection refused") || detail.contains("Connection reset") || detail.contains("Failed to connect")) {
            detail = "Cannot reach AI provider. Start Ollama or enable HuggingFace fallback (free mode needs no API key).";
        }
        return new ServiceException(
                ApplicationConstant.ERROR_CODE_INTERNAL,
                detail,
                ErrorConstant.CATEGORY.TH,
                ErrorConstant.SEVERITY.I,
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }
}
