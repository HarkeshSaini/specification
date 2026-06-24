package com.specification.service.domain.repository.security;

import com.specification.service.domain.UserRole;
import com.specification.service.domain.entity.user.AuthAccountDetail;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface AuthAccountRepository extends ReactiveMongoRepository<AuthAccountDetail, String> {

	Mono<AuthAccountDetail> findByEmail(String email);

	Mono<AuthAccountDetail> findByUsername(String username);

	Mono<AuthAccountDetail> findByEmailAndDeletedFalseAndEnabledTrue(String email);
	Mono<AuthAccountDetail> findByProfileIdAndRoleAndDeletedFalse(String profileId, UserRole role);

	Mono<Boolean> existsByEmail(String email);
	Mono<Boolean> existsByUsername(String username);
}
