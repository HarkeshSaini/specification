package com.specification.service.service.user.manager.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.domain.UserRole;
import com.specification.service.domain.repository.security.ManagerUserRepository;
import com.specification.service.request.profile.ManagerProfileRequest;
import com.specification.service.response.profile.ManagerProfileResponse;
import com.specification.service.response.user.PageResponse;
import com.specification.service.service.user.auth.IManagedAuthAccountService;
import com.specification.service.service.user.manager.IManagerUserService;
import com.specification.service.transformer.support.ReactivePageSupport;
import com.specification.service.transformer.support.UserRequestValidator;
import com.specification.service.transformer.user.manager.IManagerUserTransformer;
import com.specification.service.utility.ReactiveEntityGuards;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ManagerUserServiceImpl implements IManagerUserService {

	private static final UserRole ROLE = UserRole.MANAGER;

	private final ManagerUserRepository managerUserRepository;
	private final IManagedAuthAccountService managedAuthAccountService;
	private final IManagerUserTransformer managerUserTransformer;

	@Override
	public Mono<ManagerProfileResponse> create(ManagerProfileRequest request, String actor) {
		UserRequestValidator.requirePasswordOnCreate(request.getPassword());
		Instant now = Instant.now();
		return Mono.fromCallable(() -> managerUserTransformer.toNewEntity(request, actor, now))
				.flatMap(draft -> managedAuthAccountService.assertEmailAndUsernameAvailable(draft.getEmail(), request.getUsername())
						.then(managerUserRepository.save(draft))
						.flatMap(saved -> managedAuthAccountService
								.createForProfile(ROLE, saved.getEmail(), request.getUsername(), request.getPassword(), request.getActive(), saved.getId(), actor, now)
								.thenReturn(saved)))
				.map(managerUserTransformer::toProfileResponse);
	}

	@Override
	public Mono<ManagerProfileResponse> update(String userId, ManagerProfileRequest request, String actor) {
		Instant now = Instant.now();
		return managedAuthAccountService.findActiveForProfile(ROLE, userId)
				.flatMap(account -> ReactiveEntityGuards.orNotFound(managerUserRepository.findByIdAndDeletedFalse(userId),
						ApplicationConstant.MANAGER_PROFILE_LABEL, userId)
						.flatMap(user -> {
							managerUserTransformer.applyUpdates(user, request, actor, now);
							return managedAuthAccountService.assertEmailAndUsernameAvailableForUpdate(account, user.getEmail(), request.getUsername())
									.then(managerUserRepository.save(user))
									.flatMap(saved -> managedAuthAccountService
											.updateAccount(account, saved.getEmail(), request.getUsername(), request.getActive(), request.getPassword(), actor, now)
											.thenReturn(saved));
						}))
				.map(managerUserTransformer::toProfileResponse);
	}

	@Override
	public Mono<ManagerProfileResponse> getById(String userId) {
		return ReactiveEntityGuards.orNotFound(managerUserRepository.findByIdAndDeletedFalse(userId), ApplicationConstant.MANAGER_PROFILE_LABEL, userId)
				.map(managerUserTransformer::toProfileResponse);
	}

	@Override
	public Mono<PageResponse<ManagerProfileResponse>> list(int page, int size) {
		ReactivePageSupport.validatePage(page, size);
		long offset = (long) page * size;
		return ReactivePageSupport.collect(
				managerUserRepository.findAllByDeletedFalse().skip(offset).take(size).map(managerUserTransformer::toProfileResponse),
				managerUserRepository.countByDeletedFalse(), page, size);
	}

	@Override
	public Mono<Void> softDelete(String userId, String actor) {
		Instant now = Instant.now();
		return ReactiveEntityGuards.orNotFound(managerUserRepository.findByIdAndDeletedFalse(userId), ApplicationConstant.MANAGER_PROFILE_LABEL, userId)
				.flatMap(user -> {
					user.setDeleted(true);
					user.setDeletedAt(now);
					user.setDeletedBy(actor);
					user.setActive(Boolean.FALSE);
					user.setUpdateTimestamp(now);
					user.setUpdatedBy(actor);
					return managerUserRepository.save(user);
				})
				.then(managedAuthAccountService.softDeleteForProfile(ROLE, userId, actor, now));
	}
}
