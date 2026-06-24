package com.specification.service.utility;

import reactor.core.publisher.Mono;

import com.specification.service.exception.ResourceNotFoundException;

/**
 * Reusable reactive policies (single responsibility: empty-stream handling).
 */
public final class ReactiveEntityGuards {

	private ReactiveEntityGuards() {
	}

	public static <T> Mono<T> orNotFound(Mono<T> source, String resourceLabel, String id) {
		return source.switchIfEmpty(Mono.error(new ResourceNotFoundException(resourceLabel + " not found: " + id)));
	}
}
