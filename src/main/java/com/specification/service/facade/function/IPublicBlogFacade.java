package com.specification.service.facade.function;

import com.specification.service.response.function.BlogDetailResponse;
import com.specification.service.response.function.BlogSummaryResponse;
import com.specification.service.response.user.PageResponse;
import reactor.core.publisher.Mono;

public interface IPublicBlogFacade {

    Mono<PageResponse<BlogSummaryResponse>> listPublished(int page, int size);

    Mono<BlogDetailResponse> getBySlug(String slug);
}
