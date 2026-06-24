package com.specification.service.transformer.support;

import java.util.List;

import com.specification.service.response.user.PageResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public final class ReactivePageSupport {

	private ReactivePageSupport() {
	}

	public static <T> Mono<PageResponse<T>> collect(Flux<T> records, Mono<Long> countMono, int page, int size) {
		return records.collectList().zipWith(countMono).map(tuple -> {
			List<T> items = tuple.getT1();
			long totalElements = tuple.getT2();
			int totalPages = totalElements == 0 ? 0 : (int) Math.ceil((double) totalElements / size);
			return PageResponse.<T>builder()
					.items(items)
					.page(page)
					.size(size)
					.totalElements(totalElements)
					.totalPages(totalPages)
					.hasNext((long) (page + 1) * size < totalElements)
					.build();
		});
	}

	public static void validatePage(int page, int size) {
		if (page < 0 || size <= 0) {
			throw new IllegalArgumentException("Invalid pagination values. page must be >= 0 and size must be > 0");
		}
	}
}
