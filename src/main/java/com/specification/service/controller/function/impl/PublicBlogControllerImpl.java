package com.specification.service.controller.function.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.controller.function.IPublicBlogController;
import com.specification.service.facade.function.IPublicBlogFacade;
import com.specification.service.response.apires.APIResponse;
import com.specification.service.response.function.BlogDetailResponse;
import com.specification.service.response.function.BlogSummaryResponse;
import com.specification.service.response.user.PageResponse;
import com.specification.service.transformer.support.ReactiveUserControllerSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class PublicBlogControllerImpl implements IPublicBlogController {

    private final IPublicBlogFacade publicBlogFacade;
    private final ReactiveUserControllerSupport controllerSupport;

    @Override
    public Mono<ResponseEntity<APIResponse<PageResponse<BlogSummaryResponse>>>> list(int page, int size) {
        return controllerSupport.toOk(publicBlogFacade.listPublished(page, size), ApplicationConstant.BLOG_LIST_FETCHED_SUCCESSFULLY);
    }

    @Override
    public Mono<ResponseEntity<APIResponse<BlogDetailResponse>>> getBySlug(String slug) {
        return controllerSupport.toOk(publicBlogFacade.getBySlug(slug), ApplicationConstant.BLOG_FETCHED_SUCCESSFULLY);
    }
}
