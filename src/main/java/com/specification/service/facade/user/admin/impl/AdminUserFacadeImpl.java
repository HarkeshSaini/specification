package com.specification.service.facade.user.admin.impl;

import com.specification.service.facade.user.admin.IAdminUserFacade;
import com.specification.service.request.profile.AdminProfileRequest;
import com.specification.service.response.profile.AdminProfileResponse;
import com.specification.service.response.user.PageResponse;
import com.specification.service.service.user.admin.IAdminUserService;
import com.specification.service.service.user.auth.impl.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AdminUserFacadeImpl implements IAdminUserFacade {

	private final CurrentUserService currentUserService;
	private final IAdminUserService adminUserService;

	@Override
	public Mono<AdminProfileResponse> create(AdminProfileRequest request) {
		return currentUserService.requireUserManagementAccess()
				.flatMap(currentUser -> adminUserService.create(request, currentUser.getEmail()));
	}

	@Override
	public Mono<AdminProfileResponse> update(String userId, AdminProfileRequest request) {
		return currentUserService.requireUserManagementAccess()
				.flatMap(currentUser -> adminUserService.update(userId, request, currentUser.getEmail()));
	}

	@Override
	public Mono<AdminProfileResponse> getById(String userId) {
		return currentUserService.requireUserManagementAccess().then(adminUserService.getById(userId));
	}

	@Override
	public Mono<PageResponse<AdminProfileResponse>> list(int page, int size) {
		return currentUserService.requireUserManagementAccess().then(adminUserService.list(page, size));
	}

	@Override
	public Mono<Void> softDelete(String userId) {
		return currentUserService.requireUserManagementAccess()
				.flatMap(currentUser -> adminUserService.softDelete(userId, currentUser.getEmail()));
	}
}
