package com.specification.service.controller.profile.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.controller.profile.ISuperAdminProfileController;
import com.specification.service.facade.auth.IProfileFacade;
import com.specification.service.response.apires.APIResponse;
import com.specification.service.response.profile.SuperAdminProfileResponse;
import com.specification.service.transformer.support.ReactiveUserControllerSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class SuperAdminProfileControllerImpl implements ISuperAdminProfileController {

	private final IProfileFacade profileFacade;
	private final ReactiveUserControllerSupport controllerSupport;

	@Override
	public Mono<ResponseEntity<APIResponse<SuperAdminProfileResponse>>> me() {
		return controllerSupport.toOk(profileFacade.getSuperAdminProfile(), ApplicationConstant.SUPER_ADMIN_PROFILE_FETCHED_SUCCESSFULLY);
	}
}
