package com.specification.service.service.user.auth.impl;

import java.time.Instant;

import com.specification.service.domain.entity.user.AuthAccountDetail;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.specification.service.domain.UserRole;
 import com.specification.service.domain.repository.security.AuthAccountRepository;
import com.specification.service.exception.ConflictException;
import com.specification.service.service.user.auth.IManagedAuthAccountService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ManagedAuthAccountServiceImpl implements IManagedAuthAccountService {

	private final AuthAccountRepository authAccountRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Mono<AuthAccountDetail> findActiveForProfile(UserRole role, String profileId) {
		return authAccountRepository.findByProfileIdAndRoleAndDeletedFalse(profileId, role)
				.switchIfEmpty(Mono.error(new ConflictException("Auth account missing for profile: " + profileId)));
	}

	@Override
	public Mono<Void> assertEmailAndUsernameAvailable(String email, String username) {
		Mono<Void> emailCheck = authAccountRepository.existsByEmail(email)
				.filter(Boolean::booleanValue)
				.flatMap(exists -> Mono.error(new ConflictException("Email already exists: " + email)))
				.then();
		Mono<Void> usernameCheck = !StringUtils.hasText(username) ? Mono.empty()
				: authAccountRepository.existsByUsername(username)
						.filter(Boolean::booleanValue)
						.flatMap(exists -> Mono.error(new ConflictException("Username already exists: " + username)))
						.then();
		return emailCheck.then(usernameCheck);
	}

	@Override
	public Mono<Void> assertEmailAndUsernameAvailableForUpdate(AuthAccountDetail account, String email, String username) {
		Mono<Void> emailCheck = account.getEmail().equalsIgnoreCase(email) ? Mono.empty()
				: authAccountRepository.existsByEmail(email)
						.filter(Boolean::booleanValue)
						.flatMap(exists -> Mono.error(new ConflictException("Email already exists: " + email)))
						.then();
		Mono<Void> usernameCheck = !StringUtils.hasText(username) || username.equals(account.getUsername()) ? Mono.empty()
				: authAccountRepository.existsByUsername(username)
						.filter(Boolean::booleanValue)
						.flatMap(exists -> Mono.error(new ConflictException("Username already exists: " + username)))
						.then();
		return emailCheck.then(usernameCheck);
	}

	@Override
	public Mono<AuthAccountDetail> createForProfile(UserRole role, String email, String username, String rawPassword, Boolean active, String profileId,
			String actor, Instant now) {
        AuthAccountDetail account = AuthAccountDetail.builder()
				.email(email)
				.username(username)
				.passwordHash(passwordEncoder.encode(rawPassword))
				.enabled(resolveActive(active))
				.accountLocked(Boolean.FALSE)
				.passwordExpired(Boolean.FALSE)
				.role(role)
				.profileId(profileId)
				.failedLoginAttempts(0)
				.emailVerified(Boolean.FALSE)
				.createdTimestamp(now)
				.updateTimestamp(now)
				.createdBy(actor)
				.updatedBy(actor)
				.deleted(false)
				.build();
		return authAccountRepository.save(account);
	}

	@Override
	public Mono<Void> updateAccount(AuthAccountDetail account, String normalizedEmail, String username, Boolean active, String rawPasswordOrNull,
			String actor, Instant now) {
		account.setEmail(normalizedEmail);
		account.setUsername(username);
		account.setEnabled(resolveActive(active));
		account.setUpdateTimestamp(now);
		account.setUpdatedBy(actor);
		if (StringUtils.hasText(rawPasswordOrNull)) {
			account.setPasswordHash(passwordEncoder.encode(rawPasswordOrNull));
			account.setCredentialsUpdatedAt(now);
		}
		return authAccountRepository.save(account).then();
	}

	@Override
	public Mono<Void> softDeleteForProfile(UserRole role, String profileId, String actor, Instant now) {
		return authAccountRepository.findByProfileIdAndRoleAndDeletedFalse(profileId, role)
				.flatMap(account -> {
					account.setDeleted(true);
					account.setDeletedAt(now);
					account.setDeletedBy(actor);
					account.setEnabled(Boolean.FALSE);
					account.setUpdateTimestamp(now);
					account.setUpdatedBy(actor);
					return authAccountRepository.save(account);
				})
				.then();
	}

	private static Boolean resolveActive(Boolean active) {
		return active == null ? Boolean.TRUE : active;
	}
}
