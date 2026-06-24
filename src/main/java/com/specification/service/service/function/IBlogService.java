package com.specification.service.service.function;

import com.specification.service.request.function.BlogCreateRequest;
import com.specification.service.request.function.BlogUpdateRequest;
import com.specification.service.response.function.BlogDetailResponse;
import com.specification.service.response.function.BlogSummaryResponse;
import com.specification.service.response.user.PageResponse;
import reactor.core.publisher.Mono;

public interface IBlogService {

    Mono<BlogDetailResponse> create(BlogCreateRequest request, String actor, String authorId, String authorName);

    Mono<BlogDetailResponse> update(String id, BlogUpdateRequest request, String actor);

    Mono<BlogDetailResponse> getById(String id);

    Mono<BlogDetailResponse> getPublishedBySlug(String slug);

    Mono<PageResponse<BlogSummaryResponse>> listPublished(int page, int size);

    Mono<PageResponse<BlogSummaryResponse>> listAll(int page, int size);

    Mono<Void> softDelete(String id, String actor);
}
