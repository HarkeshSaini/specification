package com.specification.service.transformer.user.auth.impl;

import com.specification.service.domain.entity.user.AuthAccountDetail;
import org.springframework.stereotype.Component;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.domain.UserRole;
import com.specification.service.response.TokenResponse;
import com.specification.service.config.security.jwtconfig.JwtService;
import com.specification.service.transformer.user.auth.IAuthTransformer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthTransformer implements IAuthTransformer {

	private final JwtService jwtService;

	@Override
	public TokenResponse toTokenResponse(AuthAccountDetail account) {
		log.debug(ApplicationConstant.BUILDING_TOKEN_RESPONSE_FOR_AUTH_ACCOUNT, account.getId());
		String jwt = jwtService.generateToken(account);
		long expiresInSeconds = ApplicationConstant.JWT_EXPIRATION_MS / ApplicationConstant.MILLISECONDS_TO_SECONDS_DIVISOR;
		return TokenResponse.builder()
				.accessToken(jwt)
				.tokenType(ApplicationConstant.TOKEN_TYPE_BEARER)
				.expiresInSeconds(expiresInSeconds)
				.role(account.getRole().name())
				.profileId(account.getProfileId())
				.redirectPath(resolveRedirectPath(account.getRole()))
				.build();
	}

	private static String resolveRedirectPath(UserRole role) {
		return switch (role) {
			case SUPER_ADMIN -> "/admin/dashboard";
			case ADMIN -> "/admin/dashboard";
			case MANAGER -> "/manager/dashboard";
			case USER -> "/user/dashboard";
		};
	}
}
