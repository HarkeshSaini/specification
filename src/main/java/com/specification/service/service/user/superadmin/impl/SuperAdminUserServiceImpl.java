package com.specification.service.service.user.superadmin.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.domain.UserRole;
import com.specification.service.domain.repository.security.SuperAdminUserRepository;
import com.specification.service.request.profile.SuperAdminProfileRequest;
import com.specification.service.response.profile.SuperAdminProfileResponse;
import com.specification.service.response.user.PageResponse;
import com.specification.service.service.user.auth.IManagedAuthAccountService;
import com.specification.service.service.user.superadmin.ISuperAdminUserService;
import com.specification.service.transformer.support.ReactivePageSupport;
import com.specification.service.transformer.support.UserRequestValidator;
import com.specification.service.transformer.user.superadmin.ISuperAdminUserTransformer;
import com.specification.service.utility.ReactiveEntityGuards;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SuperAdminUserServiceImpl implements ISuperAdminUserService {

	private static final UserRole ROLE = UserRole.SUPER_ADMIN;

	private final SuperAdminUserRepository superAdminUserRepository;
	private final IManagedAuthAccountService managedAuthAccountService;
	private final ISuperAdminUserTransformer superAdminUserTransformer;

	@Override
	public Mono<SuperAdminProfileResponse> create(SuperAdminProfileRequest request, String actor) {
		UserRequestValidator.requirePasswordOnCreate(request.getPassword());
		Instant now = Instant.now();
		return Mono.fromCallable(() -> superAdminUserTransformer.toNewEntity(request, actor, now))
				.flatMap(draft -> managedAuthAccountService.assertEmailAndUsernameAvailable(draft.getEmail(), request.getUsername())
						.then(superAdminUserRepository.save(draft))
						.flatMap(saved -> managedAuthAccountService
								.createForProfile(ROLE, saved.getEmail(), request.getUsername(), request.getPassword(), request.getActive(), saved.getId(),
										actor, now)
								.thenReturn(saved)))
				.map(superAdminUserTransformer::toProfileResponse);
	}

	@Override
	public Mono<SuperAdminProfileResponse> update(String userId, SuperAdminProfileRequest request, String actor) {
		Instant now = Instant.now();
		return managedAuthAccountService.findActiveForProfile(ROLE, userId)
				.flatMap(account -> ReactiveEntityGuards.orNotFound(superAdminUserRepository.findByIdAndDeletedFalse(userId),
								ApplicationConstant.SUPER_ADMIN_PROFILE_LABEL, userId)
						.flatMap(user -> {
							superAdminUserTransformer.applyUpdates(user, request, actor, now);
							return managedAuthAccountService
									.assertEmailAndUsernameAvailableForUpdate(account, user.getEmail(), request.getUsername())
									.then(superAdminUserRepository.save(user))
									.flatMap(saved -> managedAuthAccountService
											.updateAccount(account, saved.getEmail(), request.getUsername(), request.getActive(), request.getPassword(), actor,
													now)
											.thenReturn(saved));
						}))
				.map(superAdminUserTransformer::toProfileResponse);
	}

	@Override
	public Mono<SuperAdminProfileResponse> getById(String userId) {
		return ReactiveEntityGuards.orNotFound(superAdminUserRepository.findByIdAndDeletedFalse(userId),
						ApplicationConstant.SUPER_ADMIN_PROFILE_LABEL, userId)
				.map(superAdminUserTransformer::toProfileResponse);
	}

	@Override
	public Mono<PageResponse<SuperAdminProfileResponse>> list(int page, int size) {
		ReactivePageSupport.validatePage(page, size);
		long offset = (long) page * size;
		return ReactivePageSupport.collect(
				superAdminUserRepository.findAllByDeletedFalse().skip(offset).take(size).map(superAdminUserTransformer::toProfileResponse),
				superAdminUserRepository.countByDeletedFalse(), page, size);
	}

	@Override
	public Mono<Void> softDelete(String userId, String actor) {
		Instant now = Instant.now();
		return ReactiveEntityGuards.orNotFound(superAdminUserRepository.findByIdAndDeletedFalse(userId),
						ApplicationConstant.SUPER_ADMIN_PROFILE_LABEL, userId)
				.flatMap(user -> {
					user.setDeleted(true);
					user.setDeletedAt(now);
					user.setDeletedBy(actor);
					user.setActive(Boolean.FALSE);
					user.setUpdateTimestamp(now);
					user.setUpdatedBy(actor);
					return superAdminUserRepository.save(user);
				})
				.then(managedAuthAccountService.softDeleteForProfile(ROLE, userId, actor, now));
	}
}
