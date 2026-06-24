package com.specification.service.controller.user.admin.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.transformer.support.ReactiveUserControllerSupport;
import com.specification.service.controller.user.admin.IAdminUserController;
import com.specification.service.facade.user.admin.IAdminUserFacade;
import com.specification.service.request.profile.AdminProfileRequest;
import com.specification.service.response.apires.APIResponse;
import com.specification.service.response.profile.AdminProfileResponse;
import com.specification.service.response.user.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequiredArgsConstructor
public class AdminUserControllerImpl implements IAdminUserController {

	private final IAdminUserFacade adminUserFacade;
	private final ReactiveUserControllerSupport controllerSupport;

	@Override
	public Mono<ResponseEntity<APIResponse<AdminProfileResponse>>> create(AdminProfileRequest request) {
		return controllerSupport.toCreated(adminUserFacade.create(request), ApplicationConstant.USER_CREATED_SUCCESSFULLY);
	}

	@Override
	public Mono<ResponseEntity<APIResponse<AdminProfileResponse>>> update(String userId, AdminProfileRequest request) {
		return controllerSupport.toOk(adminUserFacade.update(userId, request), ApplicationConstant.USER_UPDATED_SUCCESSFULLY);
	}

	@Override
	public Mono<ResponseEntity<APIResponse<AdminProfileResponse>>> getById(String userId) {
		return controllerSupport.toOk(adminUserFacade.getById(userId), ApplicationConstant.USER_FETCHED_SUCCESSFULLY);
	}

	@Override
	public Mono<ResponseEntity<APIResponse<PageResponse<AdminProfileResponse>>>> list(int page, int size) {
		return controllerSupport.toOk(adminUserFacade.list(page, size), ApplicationConstant.USER_LIST_FETCHED_SUCCESSFULLY);
	}

	@Override
	public Mono<ResponseEntity<APIResponse<Void>>> delete(String userId) {
		return controllerSupport.toOkVoid(adminUserFacade.softDelete(userId), ApplicationConstant.USER_SOFT_DELETED_SUCCESSFULLY);
	}
}
