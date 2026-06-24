package com.specification.service.controller.user.superadmin.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.controller.user.superadmin.ISuperAdminUserController;
import com.specification.service.facade.user.superadmin.ISuperAdminUserFacade;
import com.specification.service.request.profile.SuperAdminProfileRequest;
import com.specification.service.response.apires.APIResponse;
import com.specification.service.response.profile.SuperAdminProfileResponse;
import com.specification.service.response.user.PageResponse;
import com.specification.service.transformer.support.ReactiveUserControllerSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequiredArgsConstructor
public class SuperAdminUserControllerImpl implements ISuperAdminUserController {

	private final ISuperAdminUserFacade superAdminUserFacade;
	private final ReactiveUserControllerSupport controllerSupport;

	@Override
	public Mono<ResponseEntity<APIResponse<SuperAdminProfileResponse>>> create(SuperAdminProfileRequest request) {
		return controllerSupport.toCreated(superAdminUserFacade.create(request), ApplicationConstant.USER_CREATED_SUCCESSFULLY);
	}

	@Override
	public Mono<ResponseEntity<APIResponse<SuperAdminProfileResponse>>> update(String userId, SuperAdminProfileRequest request) {
		return controllerSupport.toOk(superAdminUserFacade.update(userId, request), ApplicationConstant.USER_UPDATED_SUCCESSFULLY);
	}

	@Override
	public Mono<ResponseEntity<APIResponse<SuperAdminProfileResponse>>> getById(String userId) {
		return controllerSupport.toOk(superAdminUserFacade.getById(userId), ApplicationConstant.USER_FETCHED_SUCCESSFULLY);
	}

	@Override
	public Mono<ResponseEntity<APIResponse<PageResponse<SuperAdminProfileResponse>>>> list(int page, int size) {
		return controllerSupport.toOk(superAdminUserFacade.list(page, size), ApplicationConstant.USER_LIST_FETCHED_SUCCESSFULLY);
	}

	@Override
	public Mono<ResponseEntity<APIResponse<Void>>> delete(String userId) {
		return controllerSupport.toOkVoid(superAdminUserFacade.softDelete(userId), ApplicationConstant.USER_SOFT_DELETED_SUCCESSFULLY);
	}
}
