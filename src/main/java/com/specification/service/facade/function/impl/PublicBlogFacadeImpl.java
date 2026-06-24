package com.specification.service.facade.function.impl;

import com.specification.service.facade.function.IPublicBlogFacade;
import com.specification.service.response.function.BlogDetailResponse;
import com.specification.service.response.function.BlogSummaryResponse;
import com.specification.service.response.user.PageResponse;
import com.specification.service.service.function.IBlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PublicBlogFacadeImpl implements IPublicBlogFacade {

    private final IBlogService blogService;

    @Override
    public Mono<PageResponse<BlogSummaryResponse>> listPublished(int page, int size) {
        return blogService.listPublished(page, size);
    }

    @Override
    public Mono<BlogDetailResponse> getBySlug(String slug) {
        return blogService.getPublishedBySlug(slug);
    }
}
