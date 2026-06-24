package com.specification.service.controller.user.website.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.transformer.support.ReactiveUserControllerSupport;
import com.specification.service.controller.user.website.IWebsiteUserController;
import com.specification.service.facade.user.website.IWebsiteUserFacade;
import com.specification.service.request.profile.WebsiteProfileRequest;
import com.specification.service.response.apires.APIResponse;
import com.specification.service.response.profile.WebsiteProfileResponse;
import com.specification.service.response.user.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequiredArgsConstructor
public class WebsiteUserControllerImpl implements IWebsiteUserController {

	private final IWebsiteUserFacade websiteUserFacade;
	private final ReactiveUserControllerSupport controllerSupport;

	@Override
	public Mono<ResponseEntity<APIResponse<WebsiteProfileResponse>>> create(WebsiteProfileRequest request) {
		return controllerSupport.toCreated(websiteUserFacade.create(request), ApplicationConstant.USER_CREATED_SUCCESSFULLY);
	}

	@Override
	public Mono<ResponseEntity<APIResponse<WebsiteProfileResponse>>> update(String userId, WebsiteProfileRequest request) {
		return controllerSupport.toOk(websiteUserFacade.update(userId, request), ApplicationConstant.USER_UPDATED_SUCCESSFULLY);
	}

	@Override
	public Mono<ResponseEntity<APIResponse<WebsiteProfileResponse>>> getById(String userId) {
		return controllerSupport.toOk(websiteUserFacade.getById(userId), ApplicationConstant.USER_FETCHED_SUCCESSFULLY);
	}

	@Override
	public Mono<ResponseEntity<APIResponse<PageResponse<WebsiteProfileResponse>>>> list(int page, int size) {
		return controllerSupport.toOk(websiteUserFacade.list(page, size), ApplicationConstant.USER_LIST_FETCHED_SUCCESSFULLY);
	}

	@Override
	public Mono<ResponseEntity<APIResponse<Void>>> delete(String userId) {
		return controllerSupport.toOkVoid(websiteUserFacade.softDelete(userId), ApplicationConstant.USER_SOFT_DELETED_SUCCESSFULLY);
	}
}
