package com.specification.service.facade.auth;

import com.specification.service.response.profile.AdminProfileResponse;
import com.specification.service.response.profile.ManagerProfileResponse;
import com.specification.service.response.profile.SuperAdminProfileResponse;
import com.specification.service.response.profile.WebsiteProfileResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProfileFacade {

	Mono<AdminProfileResponse> getAdminProfile();

	Mono<ManagerProfileResponse> getManagerProfile();

	Flux<WebsiteProfileResponse> listWebsiteUsers();

	Mono<SuperAdminProfileResponse> getSuperAdminProfile();

	Mono<WebsiteProfileResponse> getWebsiteUserProfile();
}
