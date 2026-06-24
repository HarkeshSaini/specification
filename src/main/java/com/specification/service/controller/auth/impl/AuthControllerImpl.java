package com.specification.service.controller.auth.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.controller.auth.IAuthController;
import com.specification.service.facade.auth.IAuthFacade;
import com.specification.service.request.LoginRequest;
import com.specification.service.request.profile.WebsiteProfileRequest;
import com.specification.service.response.TokenResponse;
import com.specification.service.response.apires.APIResponse;
import com.specification.service.response.profile.WebsiteProfileResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class AuthControllerImpl implements IAuthController {

	private final IAuthFacade authFacade;

    public AuthControllerImpl(IAuthFacade authFacade) {
        this.authFacade = authFacade;
    }

    @Override
    public Mono<ResponseEntity<APIResponse<TokenResponse>>> login(@Valid @RequestBody LoginRequest request) {
        log.info(ApplicationConstant.LOGIN_REQUEST_FOR_EMAIL, request.getEmail());
        return authFacade.login(request)
                .map(tokenResponse -> {
                    APIResponse<TokenResponse> response = APIResponse.successResponse(
                            tokenResponse,
                            ApplicationConstant.AUTH_TOKEN_CREATED_SUCCESS_MESSAGE,
                            HttpStatus.CREATED.getReasonPhrase()
                    ).getBody();
                    return ResponseEntity.ok(response);
                });
    }

    @Override
    public Mono<ResponseEntity<APIResponse<WebsiteProfileResponse>>> register(@Valid @RequestBody WebsiteProfileRequest request) {
        log.info("Register request for email={}", request.getEmail());
        return authFacade.register(request)
                .map(profile -> {
                    APIResponse<WebsiteProfileResponse> response = APIResponse.successResponse(
                            profile,
                            ApplicationConstant.USER_CREATED_SUCCESSFULLY,
                            HttpStatus.CREATED.getReasonPhrase()
                    ).getBody();
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                });
    }
}
