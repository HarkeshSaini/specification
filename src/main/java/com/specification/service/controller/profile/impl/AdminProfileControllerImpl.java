package com.specification.service.controller.profile.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.controller.profile.IAdminProfileController;
import com.specification.service.transformer.support.ReactiveUserControllerSupport;
import com.specification.service.facade.auth.IProfileFacade;
import com.specification.service.response.apires.APIResponse;
import com.specification.service.response.profile.AdminProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class AdminProfileControllerImpl implements IAdminProfileController {

	private final IProfileFacade profileFacade;
	private final ReactiveUserControllerSupport controllerSupport;

	@Override
	public Mono<ResponseEntity<APIResponse<AdminProfileResponse>>> me() {
		return controllerSupport.toOk(profileFacade.getAdminProfile(), ApplicationConstant.USER_FETCHED_SUCCESSFULLY);
	}
}
