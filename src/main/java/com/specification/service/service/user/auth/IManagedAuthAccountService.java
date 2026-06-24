package com.specification.service.service.user.auth;

import java.time.Instant;

import com.specification.service.domain.UserRole;

import com.specification.service.domain.entity.user.AuthAccountDetail;
import reactor.core.publisher.Mono;

public interface IManagedAuthAccountService {

	Mono<AuthAccountDetail> findActiveForProfile(UserRole role, String profileId);

	Mono<Void> assertEmailAndUsernameAvailable(String email, String username);

	Mono<Void> assertEmailAndUsernameAvailableForUpdate(AuthAccountDetail account, String email, String username);

	Mono<AuthAccountDetail> createForProfile(UserRole role, String email, String username, String rawPassword, Boolean active, String profileId,
			String actor, Instant now);

	Mono<Void> updateAccount(AuthAccountDetail account, String normalizedEmail, String username, Boolean active, String rawPasswordOrNull, String actor,
			Instant now);

	Mono<Void> softDeleteForProfile(UserRole role, String profileId, String actor, Instant now);
}
