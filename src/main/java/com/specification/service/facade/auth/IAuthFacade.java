package com.specification.service.facade.auth;

import com.specification.service.request.LoginRequest;
import com.specification.service.request.profile.WebsiteProfileRequest;
import com.specification.service.response.TokenResponse;
import com.specification.service.response.profile.WebsiteProfileResponse;

import reactor.core.publisher.Mono;

public interface IAuthFacade {

	Mono<TokenResponse> login(LoginRequest request);

	Mono<WebsiteProfileResponse> register(WebsiteProfileRequest request);
}
