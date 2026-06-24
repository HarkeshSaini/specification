package com.specification.service.controller.user.manager.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.transformer.support.ReactiveUserControllerSupport;
import com.specification.service.controller.user.manager.IManagerUserController;
import com.specification.service.facade.user.manager.IManagerUserFacade;
import com.specification.service.request.profile.ManagerProfileRequest;
import com.specification.service.response.apires.APIResponse;
import com.specification.service.response.profile.ManagerProfileResponse;
import com.specification.service.response.user.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequiredArgsConstructor
public class ManagerUserControllerImpl implements IManagerUserController {

	private final IManagerUserFacade managerUserFacade;
	private final ReactiveUserControllerSupport controllerSupport;

	@Override
	public Mono<ResponseEntity<APIResponse<ManagerProfileResponse>>> create(ManagerProfileRequest request) {
		return controllerSupport.toCreated(managerUserFacade.create(request), ApplicationConstant.USER_CREATED_SUCCESSFULLY);
	}

	@Override
	public Mono<ResponseEntity<APIResponse<ManagerProfileResponse>>> update(String userId, ManagerProfileRequest request) {
		return controllerSupport.toOk(managerUserFacade.update(userId, request), ApplicationConstant.USER_UPDATED_SUCCESSFULLY);
	}

	@Override
	public Mono<ResponseEntity<APIResponse<ManagerProfileResponse>>> getById(String userId) {
		return controllerSupport.toOk(managerUserFacade.getById(userId), ApplicationConstant.USER_FETCHED_SUCCESSFULLY);
	}

	@Override
	public Mono<ResponseEntity<APIResponse<PageResponse<ManagerProfileResponse>>>> list(int page, int size) {
		return controllerSupport.toOk(managerUserFacade.list(page, size), ApplicationConstant.USER_LIST_FETCHED_SUCCESSFULLY);
	}

	@Override
	public Mono<ResponseEntity<APIResponse<Void>>> delete(String userId) {
		return controllerSupport.toOkVoid(managerUserFacade.softDelete(userId), ApplicationConstant.USER_SOFT_DELETED_SUCCESSFULLY);
	}
}
