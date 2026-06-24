package com.specification.service.facade.auth.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.facade.auth.IAuthFacade;
import com.specification.service.request.LoginRequest;
import com.specification.service.request.profile.WebsiteProfileRequest;
import com.specification.service.response.TokenResponse;
import com.specification.service.response.profile.WebsiteProfileResponse;
import com.specification.service.service.user.auth.IAuthService;
import com.specification.service.service.user.website.IWebsiteUserService;
import com.specification.service.transformer.user.auth.IAuthTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthFacadeImpl implements IAuthFacade {

	private final IAuthService authService;
	private final IAuthTransformer authTransformer;
	private final IWebsiteUserService websiteUserService;

    public AuthFacadeImpl(IAuthService authService, IAuthTransformer authTransformer, IWebsiteUserService websiteUserService) {
        this.authService = authService;
        this.authTransformer = authTransformer;
        this.websiteUserService = websiteUserService;
    }

    @Override
    public Mono<TokenResponse> login(LoginRequest request) {
        return authService.authenticate(request)
                .map(authenticate -> {
                    TokenResponse token = this.authTransformer.toTokenResponse(authenticate);
                    log.debug(ApplicationConstant.ISSUED_ACCESS_TOKEN_FOR_ACCOUNT, authenticate.getId(), authenticate.getRole());
                    return token;
                });
    }

    @Override
    public Mono<WebsiteProfileResponse> register(WebsiteProfileRequest request) {
        return websiteUserService.create(request, "self-registration");
    }
}
