package com.specification.service.service.user.auth.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.domain.entity.user.AuthAccountDetail;
import com.specification.service.domain.repository.security.AuthAccountRepository;
import com.specification.service.exception.ErrorConstant;
import com.specification.service.request.LoginRequest;
import com.specification.service.response.apires.ServiceException;
import com.specification.service.service.user.auth.IAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Locale;
import java.util.Objects;

@Slf4j
@Service
public class AuthServiceImpl implements IAuthService {

	private final AuthAccountRepository authAccountRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthServiceImpl(AuthAccountRepository authAccountRepository, PasswordEncoder passwordEncoder) {
		this.authAccountRepository = Objects.requireNonNull(authAccountRepository, "authAccountRepository must not be null");
		this.passwordEncoder = Objects.requireNonNull(passwordEncoder, "passwordEncoder must not be null");
	}

	@Override
	public Mono<AuthAccountDetail> authenticate(LoginRequest request) {
		if (request == null || !StringUtils.hasText(request.getEmail()) || !StringUtils.hasText(request.getPassword())) {
			return Mono.error(missingCredentials("Email and password are required."));
		}

		String identifier = request.getEmail().trim();
		String password = request.getPassword();

		return findByEmailOrUsername(identifier)
				.switchIfEmpty(Mono.error(invalidCredentialsException()))
				.filter(acc -> StringUtils.hasText(acc.getPasswordHash()))
				.filter(acc -> !acc.isDeleted())
				.filter(acc -> !Boolean.FALSE.equals(acc.getEnabled()))
				.filter(acc -> !Boolean.TRUE.equals(acc.getAccountLocked()))
				.filter(acc -> passwordEncoder.matches(password, acc.getPasswordHash()))
				.switchIfEmpty(Mono.error(invalidCredentialsException()))
				.flatMap(this::recordSuccessfulLogin)
				.doOnNext(account -> log.info(ApplicationConstant.AUTHENTICATION_SUCCESSFUL_FOR_EMAIL, account.getEmail()));
	}

	private Mono<AuthAccountDetail> findByEmailOrUsername(String identifier) {
		String normalizedEmail = identifier.toLowerCase(Locale.ROOT);
		return authAccountRepository.findByEmail(normalizedEmail)
				.switchIfEmpty(Mono.defer(() -> authAccountRepository.findByUsername(identifier)));
	}

	private Mono<AuthAccountDetail> recordSuccessfulLogin(AuthAccountDetail account) {
		Instant now = Instant.now();
		account.setLastLoginAt(now);
		account.setFailedLoginAttempts(0);
		account.setUpdateTimestamp(now);
		return authAccountRepository.save(account);
	}

	private static ServiceException missingCredentials(String message) {
		return new ServiceException(
				ApplicationConstant.MSG_MISSING_USER_PASSWORD,
				message,
				ErrorConstant.CATEGORY.TD,
				ErrorConstant.SEVERITY.I
		);
	}

	private static BadCredentialsException invalidCredentialsException() {
		return new BadCredentialsException(ApplicationConstant.INVALID_CREDENTIALS);
	}
}
