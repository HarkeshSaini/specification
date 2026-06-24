package com.specification.service.facade.function;

import com.specification.service.request.function.BlogCreateRequest;
import com.specification.service.request.function.BlogUpdateRequest;
import com.specification.service.response.function.BlogDetailResponse;
import com.specification.service.response.function.BlogSummaryResponse;
import com.specification.service.response.user.PageResponse;
import reactor.core.publisher.Mono;

public interface IAdminBlogFacade {

    Mono<BlogDetailResponse> create(BlogCreateRequest request);

    Mono<BlogDetailResponse> update(String id, BlogUpdateRequest request);

    Mono<BlogDetailResponse> getById(String id);

    Mono<PageResponse<BlogSummaryResponse>> list(int page, int size);

    Mono<Void> delete(String id);
}
