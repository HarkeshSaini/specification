package com.specification.service.facade.auth.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.domain.UserRole;
import com.specification.service.facade.auth.IProfileFacade;
import com.specification.service.response.profile.AdminProfileResponse;
import com.specification.service.response.profile.ManagerProfileResponse;
import com.specification.service.response.profile.SuperAdminProfileResponse;
import com.specification.service.response.profile.WebsiteProfileResponse;
import com.specification.service.service.user.auth.impl.CurrentUserService;
import com.specification.service.service.user.profile.IProfileService;
import com.specification.service.transformer.user.auth.IProfileTransformer;
import com.specification.service.utility.ReactiveEntityGuards;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileFacadeImpl implements IProfileFacade {

	private final CurrentUserService currentUserService;
	private final IProfileService profileService;
	private final IProfileTransformer profileTransformer;

	@Override
	public Mono<AdminProfileResponse> getAdminProfile() {
		log.debug(ApplicationConstant.RESOLVING_ADMIN_PROFILE_FOR_CURRENT_USER);
		return currentUserService.requireRole(UserRole.ADMIN).flatMap(u ->
                ReactiveEntityGuards.orNotFound(profileService.findAdminById(u.getProfileId()), ApplicationConstant.ADMIN_PROFILE_LABEL,u.getProfileId())).map(profileTransformer::toAdminResponse);
	}

	@Override
	public Mono<ManagerProfileResponse> getManagerProfile() {
		log.debug(ApplicationConstant.RESOLVING_MANAGER_PROFILE_FOR_CURRENT_USER);
		return currentUserService.requireRole(UserRole.MANAGER).flatMap(u ->
                ReactiveEntityGuards.orNotFound(profileService.findManagerById(u.getProfileId()), ApplicationConstant.MANAGER_PROFILE_LABEL, u.getProfileId())).map(profileTransformer::toManagerResponse);
	}

	@Override
	public Flux<WebsiteProfileResponse> listWebsiteUsers() {
		log.debug(ApplicationConstant.LISTING_WEBSITE_USERS_MANAGER_FLOW);
		return profileService.findAllWebsiteUsers().map(profileTransformer::toWebsiteResponse);
	}

	@Override
	public Mono<SuperAdminProfileResponse> getSuperAdminProfile() {
		log.debug(ApplicationConstant.RESOLVING_SUPER_ADMIN_PROFILE_FOR_CURRENT_USER);
		return currentUserService.requireSuperAdmin().flatMap(u ->
				ReactiveEntityGuards.orNotFound(profileService.findSuperAdminById(u.getProfileId()), ApplicationConstant.SUPER_ADMIN_PROFILE_LABEL,
						u.getProfileId())).map(profileTransformer::toSuperAdminResponse);
	}

	@Override
	public Mono<WebsiteProfileResponse> getWebsiteUserProfile() {
		log.debug(ApplicationConstant.RESOLVING_WEBSITE_USER_PROFILE_FOR_CURRENT_USER);
		return currentUserService.requireRole(UserRole.USER).flatMap(u ->
                ReactiveEntityGuards.orNotFound(profileService.findWebsiteUserById(u.getProfileId()), ApplicationConstant.WEBSITE_USER_PROFILE_LABEL, u.getProfileId())).map(profileTransformer::toWebsiteResponse);
	}
}
