package com.specification.service.transformer.support;

import com.specification.service.response.apires.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Shared controller helper — keeps user CRUD controllers thin (Single Responsibility).
 */
@Component
public class ReactiveUserControllerSupport {

    public <T> ResponseEntity<APIResponse<T>> created(T data, String message) {
        APIResponse<T> body = APIResponse.<T>builder().status("SUCCESS").httpStatus(HttpStatus.CREATED.value()).message(message).data(data).code(HttpStatus.CREATED.getReasonPhrase()).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    public <T> ResponseEntity<APIResponse<T>> ok(T data, String message) {
        APIResponse<T> body = APIResponse.<T>builder().status("SUCCESS").httpStatus(HttpStatus.OK.value()).message(message).data(data).code(HttpStatus.OK.getReasonPhrase()).build();
        return ResponseEntity.ok(body);
    }

    public <T> Mono<ResponseEntity<APIResponse<T>>> toCreated(Mono<T> publisher, String message) {
        return publisher.map(data -> created(data, message));
    }

    public <T> Mono<ResponseEntity<APIResponse<T>>> toOk(Mono<T> publisher, String message) {
        return publisher.map(data -> ok(data, message));
    }

    public Mono<ResponseEntity<APIResponse<Void>>> toOkVoid(Mono<Void> publisher, String message) {
        return publisher.then(Mono.fromSupplier(() -> ok(null, message)));
    }
}
