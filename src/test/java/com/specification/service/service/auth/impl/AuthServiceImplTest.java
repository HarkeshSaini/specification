package com.specification.service.service.auth.impl;

import static org.mockito.Mockito.when;

import com.specification.service.domain.entity.user.AuthAccountDetail;
import com.specification.service.service.user.auth.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.specification.service.domain.UserRole;
import com.specification.service.domain.repository.security.AuthAccountRepository;
import com.specification.service.request.LoginRequest;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

	@Mock
	private AuthAccountRepository authAccountRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private AuthServiceImpl authService;

	@Test
	void authenticate_rejectsMissingUsers() {
		LoginRequest request = new LoginRequest();
		request.setEmail("test@mail.com");
		request.setPassword("secret");

		when(authAccountRepository.findByEmail("test@mail.com")).thenReturn(Mono.empty());
		when(authAccountRepository.findByUsername("test@mail.com")).thenReturn(Mono.empty());

		StepVerifier.create(authService.authenticate(request))
				.expectError(BadCredentialsException.class)
				.verify();
	}

	@Test
	void authenticate_acceptsValidCredential() {
		LoginRequest request = new LoginRequest();
		request.setEmail("test@mail.com");
		request.setPassword("secret");

        AuthAccountDetail account = AuthAccountDetail.builder()
				.email("test@mail.com")
				.passwordHash("hash")
				.enabled(Boolean.TRUE)
				.accountLocked(Boolean.FALSE)
				.deleted(false)
				.role(UserRole.ADMIN)
				.build();

		when(authAccountRepository.findByEmail("test@mail.com")).thenReturn(Mono.just(account));
		when(passwordEncoder.matches("secret", "hash")).thenReturn(true);
		when(authAccountRepository.save(account)).thenReturn(Mono.just(account));

		StepVerifier.create(authService.authenticate(request))
				.expectNext(account)
				.verifyComplete();
	}
}
