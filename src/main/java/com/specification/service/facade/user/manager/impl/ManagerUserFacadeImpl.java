package com.specification.service.facade.user.manager.impl;

import com.specification.service.facade.user.manager.IManagerUserFacade;
import com.specification.service.request.profile.ManagerProfileRequest;
import com.specification.service.response.profile.ManagerProfileResponse;
import com.specification.service.response.user.PageResponse;
import com.specification.service.service.user.auth.impl.CurrentUserService;
import com.specification.service.service.user.manager.IManagerUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ManagerUserFacadeImpl implements IManagerUserFacade {

	private final CurrentUserService currentUserService;
	private final IManagerUserService managerUserService;

	@Override
	public Mono<ManagerProfileResponse> create(ManagerProfileRequest request) {
		return currentUserService.requireUserManagementAccess()
				.flatMap(currentUser -> managerUserService.create(request, currentUser.getEmail()));
	}

	@Override
	public Mono<ManagerProfileResponse> update(String userId, ManagerProfileRequest request) {
		return currentUserService.requireUserManagementAccess()
				.flatMap(currentUser -> managerUserService.update(userId, request, currentUser.getEmail()));
	}

	@Override
	public Mono<ManagerProfileResponse> getById(String userId) {
		return currentUserService.requireUserManagementAccess().then(managerUserService.getById(userId));
	}

	@Override
	public Mono<PageResponse<ManagerProfileResponse>> list(int page, int size) {
		return currentUserService.requireUserManagementAccess().then(managerUserService.list(page, size));
	}

	@Override
	public Mono<Void> softDelete(String userId) {
		return currentUserService.requireUserManagementAccess()
				.flatMap(currentUser -> managerUserService.softDelete(userId, currentUser.getEmail()));
	}
}
