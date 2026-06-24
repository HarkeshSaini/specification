package com.specification.service.facade.function.impl;

import com.specification.service.facade.function.IAdminBlogFacade;
import com.specification.service.request.function.BlogCreateRequest;
import com.specification.service.request.function.BlogUpdateRequest;
import com.specification.service.response.function.BlogDetailResponse;
import com.specification.service.response.function.BlogSummaryResponse;
import com.specification.service.response.user.PageResponse;
import com.specification.service.service.function.IBlogService;
import com.specification.service.service.user.auth.impl.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AdminBlogFacadeImpl implements IAdminBlogFacade {

    private final CurrentUserService currentUserService;
    private final IBlogService blogService;

    @Override
    public Mono<BlogDetailResponse> create(BlogCreateRequest request) {
        return currentUserService.requireUserManagementAccess()
                .flatMap(user -> blogService.create(request, user.getEmail(), user.getProfileId(), user.getEmail()));
    }

    @Override
    public Mono<BlogDetailResponse> update(String id, BlogUpdateRequest request) {
        return currentUserService.requireUserManagementAccess()
                .flatMap(user -> blogService.update(id, request, user.getEmail()));
    }

    @Override
    public Mono<BlogDetailResponse> getById(String id) {
        return currentUserService.requireUserManagementAccess().then(blogService.getById(id));
    }

    @Override
    public Mono<PageResponse<BlogSummaryResponse>> list(int page, int size) {
        return currentUserService.requireUserManagementAccess().then(blogService.listAll(page, size));
    }

    @Override
    public Mono<Void> delete(String id) {
        return currentUserService.requireSuperAdmin()
                .flatMap(user -> blogService.softDelete(id, user.getEmail()));
    }
}
