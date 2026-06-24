package com.specification.service.controller.profile.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.controller.profile.IWebsiteProfileController;
import com.specification.service.transformer.support.ReactiveUserControllerSupport;
import com.specification.service.facade.auth.IProfileFacade;
import com.specification.service.response.apires.APIResponse;
import com.specification.service.response.profile.WebsiteProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class WebsiteProfileControllerImpl implements IWebsiteProfileController {

	private final IProfileFacade profileFacade;
	private final ReactiveUserControllerSupport controllerSupport;

	@Override
	public Mono<ResponseEntity<APIResponse<WebsiteProfileResponse>>> me() {
		return controllerSupport.toOk(profileFacade.getWebsiteUserProfile(), ApplicationConstant.USER_FETCHED_SUCCESSFULLY);
	}
}
