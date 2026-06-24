package com.specification.service.facade.user.website.impl;

import com.specification.service.facade.user.website.IWebsiteUserFacade;
import com.specification.service.request.profile.WebsiteProfileRequest;
import com.specification.service.response.profile.WebsiteProfileResponse;
import com.specification.service.response.user.PageResponse;
import com.specification.service.service.user.auth.impl.CurrentUserService;
import com.specification.service.service.user.website.IWebsiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WebsiteUserFacadeImpl implements IWebsiteUserFacade {

	private final CurrentUserService currentUserService;
	private final IWebsiteUserService websiteUserService;

	@Override
	public Mono<WebsiteProfileResponse> create(WebsiteProfileRequest request) {
		return currentUserService.requireUserManagementAccess()
				.flatMap(currentUser -> websiteUserService.create(request, currentUser.getEmail()));
	}

	@Override
	public Mono<WebsiteProfileResponse> update(String userId, WebsiteProfileRequest request) {
		return currentUserService.requireUserManagementAccess()
				.flatMap(currentUser -> websiteUserService.update(userId, request, currentUser.getEmail()));
	}

	@Override
	public Mono<WebsiteProfileResponse> getById(String userId) {
		return currentUserService.requireUserManagementAccess().then(websiteUserService.getById(userId));
	}

	@Override
	public Mono<PageResponse<WebsiteProfileResponse>> list(int page, int size) {
		return currentUserService.requireUserManagementAccess().then(websiteUserService.list(page, size));
	}

	@Override
	public Mono<Void> softDelete(String userId) {
		return currentUserService.requireUserManagementAccess()
				.flatMap(currentUser -> websiteUserService.softDelete(userId, currentUser.getEmail()));
	}
}
