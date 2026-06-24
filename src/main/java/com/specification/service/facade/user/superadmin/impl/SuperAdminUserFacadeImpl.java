package com.specification.service.facade.user.superadmin.impl;

import com.specification.service.facade.user.superadmin.ISuperAdminUserFacade;
import com.specification.service.request.profile.SuperAdminProfileRequest;
import com.specification.service.response.profile.SuperAdminProfileResponse;
import com.specification.service.response.user.PageResponse;
import com.specification.service.service.user.auth.impl.CurrentUserService;
import com.specification.service.service.user.superadmin.ISuperAdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SuperAdminUserFacadeImpl implements ISuperAdminUserFacade {

	private final CurrentUserService currentUserService;
	private final ISuperAdminUserService superAdminUserService;

	@Override
	public Mono<SuperAdminProfileResponse> create(SuperAdminProfileRequest request) {
		return currentUserService.requireSuperAdmin()
				.flatMap(currentUser -> superAdminUserService.create(request, currentUser.getEmail()));
	}

	@Override
	public Mono<SuperAdminProfileResponse> update(String userId, SuperAdminProfileRequest request) {
		return currentUserService.requireSuperAdmin()
				.flatMap(currentUser -> superAdminUserService.update(userId, request, currentUser.getEmail()));
	}

	@Override
	public Mono<SuperAdminProfileResponse> getById(String userId) {
		return currentUserService.requireSuperAdmin().then(superAdminUserService.getById(userId));
	}

	@Override
	public Mono<PageResponse<SuperAdminProfileResponse>> list(int page, int size) {
		return currentUserService.requireSuperAdmin().then(superAdminUserService.list(page, size));
	}

	@Override
	public Mono<Void> softDelete(String userId) {
		return currentUserService.requireSuperAdmin()
				.flatMap(currentUser -> superAdminUserService.softDelete(userId, currentUser.getEmail()));
	}
}
