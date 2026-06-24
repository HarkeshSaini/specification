package com.specification.service.service.user.admin.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.domain.UserRole;
import com.specification.service.domain.repository.security.AdminUserRepository;
import com.specification.service.request.profile.AdminProfileRequest;
import com.specification.service.response.profile.AdminProfileResponse;
import com.specification.service.response.user.PageResponse;
import com.specification.service.service.user.admin.IAdminUserService;
import com.specification.service.service.user.auth.IManagedAuthAccountService;
import com.specification.service.transformer.support.ReactivePageSupport;
import com.specification.service.transformer.support.UserRequestValidator;
import com.specification.service.transformer.user.admin.IAdminUserTransformer;
import com.specification.service.utility.ReactiveEntityGuards;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements IAdminUserService {

	private static final UserRole ROLE = UserRole.ADMIN;

	private final AdminUserRepository adminUserRepository;
	private final IManagedAuthAccountService managedAuthAccountService;
	private final IAdminUserTransformer adminUserTransformer;

	@Override
	public Mono<AdminProfileResponse> create(AdminProfileRequest request, String actor) {
		UserRequestValidator.requirePasswordOnCreate(request.getPassword());
		Instant now = Instant.now();
		return Mono.fromCallable(() -> adminUserTransformer.toNewEntity(request, actor, now))
				.flatMap(draft -> managedAuthAccountService.assertEmailAndUsernameAvailable(draft.getEmail(), request.getUsername())
						.then(adminUserRepository.save(draft))
						.flatMap(saved -> managedAuthAccountService
								.createForProfile(ROLE, saved.getEmail(), request.getUsername(), request.getPassword(), request.getActive(), saved.getId(),
										actor, now)
								.thenReturn(saved)))
				.map(adminUserTransformer::toProfileResponse);
	}

	@Override
	public Mono<AdminProfileResponse> update(String userId, AdminProfileRequest request, String actor) {
		Instant now = Instant.now();
		return managedAuthAccountService.findActiveForProfile(ROLE, userId)
				.flatMap(account -> ReactiveEntityGuards.orNotFound(adminUserRepository.findByIdAndDeletedFalse(userId), ApplicationConstant.ADMIN_PROFILE_LABEL,
						userId)
						.flatMap(user -> {
							adminUserTransformer.applyUpdates(user, request, actor, now);
							return managedAuthAccountService
									.assertEmailAndUsernameAvailableForUpdate(account, user.getEmail(), request.getUsername())
									.then(adminUserRepository.save(user))
									.flatMap(saved -> managedAuthAccountService
											.updateAccount(account, saved.getEmail(), request.getUsername(), request.getActive(), request.getPassword(), actor,
													now)
											.thenReturn(saved));
						}))
				.map(adminUserTransformer::toProfileResponse);
	}

	@Override
	public Mono<AdminProfileResponse> getById(String userId) {
		return ReactiveEntityGuards.orNotFound(adminUserRepository.findByIdAndDeletedFalse(userId), ApplicationConstant.ADMIN_PROFILE_LABEL, userId)
				.map(adminUserTransformer::toProfileResponse);
	}

	@Override
	public Mono<PageResponse<AdminProfileResponse>> list(int page, int size) {
		ReactivePageSupport.validatePage(page, size);
		long offset = (long) page * size;
		return ReactivePageSupport.collect(
				adminUserRepository.findAllByDeletedFalse().skip(offset).take(size).map(adminUserTransformer::toProfileResponse),
				adminUserRepository.countByDeletedFalse(), page, size);
	}

	@Override
	public Mono<Void> softDelete(String userId, String actor) {
		Instant now = Instant.now();
		return ReactiveEntityGuards.orNotFound(adminUserRepository.findByIdAndDeletedFalse(userId), ApplicationConstant.ADMIN_PROFILE_LABEL, userId)
				.flatMap(user -> {
					user.setDeleted(true);
					user.setDeletedAt(now);
					user.setDeletedBy(actor);
					user.setActive(Boolean.FALSE);
					user.setUpdateTimestamp(now);
					user.setUpdatedBy(actor);
					return adminUserRepository.save(user);
				})
				.then(managedAuthAccountService.softDeleteForProfile(ROLE, userId, actor, now));
	}
}
