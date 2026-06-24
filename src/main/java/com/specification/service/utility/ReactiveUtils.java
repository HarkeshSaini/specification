package com.specification.service.utility;

import reactor.core.publisher.Mono;

public final class ReactiveUtils {

	private ReactiveUtils() {
	}

	public static <T> Mono<T> deferIfEmpty(Mono<T> source, Mono<T> ifEmpty) {
		return source.switchIfEmpty(ifEmpty);
	}
}
