package com.specification.service.controller.profile.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.controller.profile.IManagerProfileController;
import com.specification.service.transformer.support.ReactiveUserControllerSupport;
import com.specification.service.facade.auth.IProfileFacade;
import com.specification.service.response.apires.APIResponse;
import com.specification.service.response.profile.ManagerProfileResponse;
import com.specification.service.response.profile.WebsiteProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ManagerProfileControllerImpl implements IManagerProfileController {

	private final IProfileFacade profileFacade;
	private final ReactiveUserControllerSupport controllerSupport;

	@Override
	public Mono<ResponseEntity<APIResponse<ManagerProfileResponse>>> me() {
		return controllerSupport.toOk(profileFacade.getManagerProfile(), ApplicationConstant.USER_FETCHED_SUCCESSFULLY);
	}

	@Override
	public Flux<ResponseEntity<APIResponse<WebsiteProfileResponse>>> listWebsiteUsers() {
		return profileFacade.listWebsiteUsers().map(user -> controllerSupport.ok(user, ApplicationConstant.USER_LIST_FETCHED_SUCCESSFULLY));
	}
}
