package com.specification.service.service.user.website.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.domain.UserRole;
import com.specification.service.domain.repository.security.WebsiteUserRepository;
import com.specification.service.request.profile.WebsiteProfileRequest;
import com.specification.service.response.profile.WebsiteProfileResponse;
import com.specification.service.response.user.PageResponse;
import com.specification.service.service.user.auth.IManagedAuthAccountService;
import com.specification.service.transformer.support.ReactivePageSupport;
import com.specification.service.transformer.support.UserRequestValidator;
import com.specification.service.service.user.website.IWebsiteUserService;
import com.specification.service.transformer.user.website.IWebsiteUserTransformer;
import com.specification.service.utility.ReactiveEntityGuards;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class WebsiteUserServiceImpl implements IWebsiteUserService {

	private static final UserRole ROLE = UserRole.USER;

	private final WebsiteUserRepository websiteUserRepository;
	private final IManagedAuthAccountService managedAuthAccountService;
	private final IWebsiteUserTransformer websiteUserTransformer;

	@Override
	public Mono<WebsiteProfileResponse> create(WebsiteProfileRequest request, String actor) {
		UserRequestValidator.requirePasswordOnCreate(request.getPassword());
		Instant now = Instant.now();
		return Mono.fromCallable(() -> websiteUserTransformer.toNewEntity(request, actor, now))
				.flatMap(draft -> managedAuthAccountService.assertEmailAndUsernameAvailable(draft.getEmail(), request.getUsername())
						.then(websiteUserRepository.save(draft))
						.flatMap(saved -> managedAuthAccountService
								.createForProfile(ROLE, saved.getEmail(), request.getUsername(), request.getPassword(), request.getActive(), saved.getId(),
										actor, now)
								.thenReturn(saved)))
				.map(websiteUserTransformer::toProfileResponse);
	}

	@Override
	public Mono<WebsiteProfileResponse> update(String userId, WebsiteProfileRequest request, String actor) {
		Instant now = Instant.now();
		return managedAuthAccountService.findActiveForProfile(ROLE, userId)
				.flatMap(account -> ReactiveEntityGuards.orNotFound(websiteUserRepository.findByIdAndDeletedFalse(userId),
						ApplicationConstant.WEBSITE_USER_PROFILE_LABEL, userId)
						.flatMap(user -> {
							websiteUserTransformer.applyUpdates(user, request, actor, now);
							return managedAuthAccountService
									.assertEmailAndUsernameAvailableForUpdate(account, user.getEmail(), request.getUsername())
									.then(websiteUserRepository.save(user))
									.flatMap(saved -> managedAuthAccountService
											.updateAccount(account, saved.getEmail(), request.getUsername(), request.getActive(), request.getPassword(), actor,
													now)
											.thenReturn(saved));
						}))
				.map(websiteUserTransformer::toProfileResponse);
	}

	@Override
	public Mono<WebsiteProfileResponse> getById(String userId) {
		return ReactiveEntityGuards.orNotFound(websiteUserRepository.findByIdAndDeletedFalse(userId), ApplicationConstant.WEBSITE_USER_PROFILE_LABEL, userId)
				.map(websiteUserTransformer::toProfileResponse);
	}

	@Override
	public Mono<PageResponse<WebsiteProfileResponse>> list(int page, int size) {
		ReactivePageSupport.validatePage(page, size);
		long offset = (long) page * size;
		return ReactivePageSupport.collect(
				websiteUserRepository.findAllByDeletedFalse().skip(offset).take(size).map(websiteUserTransformer::toProfileResponse),
				websiteUserRepository.countByDeletedFalse(), page, size);
	}

	@Override
	public Mono<Void> softDelete(String userId, String actor) {
		Instant now = Instant.now();
		return ReactiveEntityGuards.orNotFound(websiteUserRepository.findByIdAndDeletedFalse(userId),
                        ApplicationConstant.WEBSITE_USER_PROFILE_LABEL, userId)
				.flatMap(user -> {
					user.setDeleted(true);
					user.setDeletedAt(now);
					user.setDeletedBy(actor);
					user.setActive(Boolean.FALSE);
					user.setUpdateTimestamp(now);
					return websiteUserRepository.save(user);
				})
				.then(managedAuthAccountService.softDeleteForProfile(ROLE, userId, actor, now));
	}
}
