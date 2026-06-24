package com.specification.service.response.user;

import java.util.List;

import lombok.Builder;

@Builder
public record PageResponse<T>(List<T> items, int page, int size, long totalElements, int totalPages, boolean hasNext) {
}
