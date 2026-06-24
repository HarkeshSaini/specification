package com.specification.service.controller.function.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.controller.function.IAdminBlogController;
import com.specification.service.facade.function.IAdminBlogFacade;
import com.specification.service.request.function.BlogCreateRequest;
import com.specification.service.request.function.BlogUpdateRequest;
import com.specification.service.response.apires.APIResponse;
import com.specification.service.response.function.BlogDetailResponse;
import com.specification.service.response.function.BlogSummaryResponse;
import com.specification.service.response.user.PageResponse;
import com.specification.service.transformer.support.ReactiveUserControllerSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequiredArgsConstructor
public class AdminBlogControllerImpl implements IAdminBlogController {

    private final IAdminBlogFacade adminBlogFacade;
    private final ReactiveUserControllerSupport controllerSupport;

    @Override
    public Mono<ResponseEntity<APIResponse<BlogDetailResponse>>> create(BlogCreateRequest request) {
        return controllerSupport.toCreated(adminBlogFacade.create(request), ApplicationConstant.BLOG_CREATED_SUCCESSFULLY);
    }

    @Override
    public Mono<ResponseEntity<APIResponse<BlogDetailResponse>>> update(String id, BlogUpdateRequest request) {
        return controllerSupport.toOk(adminBlogFacade.update(id, request), ApplicationConstant.BLOG_UPDATED_SUCCESSFULLY);
    }

    @Override
    public Mono<ResponseEntity<APIResponse<BlogDetailResponse>>> getById(String id) {
        return controllerSupport.toOk(adminBlogFacade.getById(id), ApplicationConstant.BLOG_FETCHED_SUCCESSFULLY);
    }

    @Override
    public Mono<ResponseEntity<APIResponse<PageResponse<BlogSummaryResponse>>>> list(int page, int size) {
        return controllerSupport.toOk(adminBlogFacade.list(page, size), ApplicationConstant.BLOG_LIST_FETCHED_SUCCESSFULLY);
    }

    @Override
    public Mono<ResponseEntity<APIResponse<Void>>> delete(String id) {
        return controllerSupport.toOkVoid(adminBlogFacade.delete(id), ApplicationConstant.BLOG_DELETED_SUCCESSFULLY);
    }
}
