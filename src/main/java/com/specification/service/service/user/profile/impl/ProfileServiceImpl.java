package com.specification.service.service.user.profile.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.domain.entity.user.AdminUserDetail;
import com.specification.service.domain.entity.user.ManagerUserDetail;
import com.specification.service.domain.entity.user.SuperAdminUserDetail;
import com.specification.service.domain.entity.user.WebUserDetail;
import com.specification.service.domain.repository.security.AdminUserRepository;
import com.specification.service.domain.repository.security.ManagerUserRepository;
import com.specification.service.domain.repository.security.SuperAdminUserRepository;
import com.specification.service.domain.repository.security.WebsiteUserRepository;
import com.specification.service.service.user.profile.IProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements IProfileService {

	private final AdminUserRepository adminUserRepository;
	private final ManagerUserRepository managerUserRepository;
	private final SuperAdminUserRepository superAdminUserRepository;
	private final WebsiteUserRepository websiteUserRepository;

	@Override
	public Mono<AdminUserDetail> findAdminById(String id) {
		log.debug(ApplicationConstant.LOADING_ADMIN_PROFILE_ID, id);
		return adminUserRepository.findById(id).filter(user -> !user.isDeleted());
	}

	@Override
	public Mono<ManagerUserDetail> findManagerById(String id) {
		log.debug(ApplicationConstant.LOADING_MANAGER_PROFILE_ID, id);
		return managerUserRepository.findById(id).filter(user -> !user.isDeleted());
	}

	@Override
	public Mono<SuperAdminUserDetail> findSuperAdminById(String id) {
		log.debug(ApplicationConstant.LOADING_SUPER_ADMIN_PROFILE_ID, id);
		return superAdminUserRepository.findById(id).filter(user -> !user.isDeleted());
	}

	@Override
	public Mono<WebUserDetail> findWebsiteUserById(String id) {
		log.debug(ApplicationConstant.LOADING_WEBSITE_USER_PROFILE_ID, id);
		return websiteUserRepository.findById(id).filter(user -> !user.isDeleted());
	}

	@Override
	public Flux<WebUserDetail> findAllWebsiteUsers() {
		log.debug(ApplicationConstant.LISTING_ALL_WEBSITE_USERS);
		return websiteUserRepository.findAll().filter(user -> !user.isDeleted());
	}
}
