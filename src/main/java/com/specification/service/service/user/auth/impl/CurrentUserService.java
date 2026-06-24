package com.specification.service.service.user.auth.impl;

import com.specification.service.constant.ApplicationConstant;
import com.specification.service.domain.UserRole;
import com.specification.service.domain.UserRoleHierarchy;
import com.specification.service.config.security.CustomUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CurrentUserService {

	public Mono<CustomUser> currentUser() {
		return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication).filter(Authentication::isAuthenticated).map(Authentication::getPrincipal).cast(CustomUser.class);
	}

	public Mono<CustomUser> requireRole(UserRole role) {
		return currentUser().filter(u -> u.getRole() == role).switchIfEmpty(Mono.defer(() -> {
			log.debug(ApplicationConstant.ROLE_CHECK_FAILED_REQUIRED, role);
			return Mono.error(new AccessDeniedException(ApplicationConstant.REQUIRES_ROLE_PREFIX + role));
		}));
	}

	public Mono<CustomUser> requireAdmin() {
		return currentUser().filter(u -> u.getRole() == UserRole.ADMIN).switchIfEmpty(Mono.defer(() -> {
			log.debug(ApplicationConstant.ROLE_CHECK_FAILED_ADMIN);
			return Mono.error(new AccessDeniedException(ApplicationConstant.REQUIRES_ADMIN));
		}));
	}

	/** ADMIN or SUPER_ADMIN — manage admins, managers, and website users. */
	public Mono<CustomUser> requireUserManagementAccess() {
		return currentUser().filter(u -> UserRoleHierarchy.canManageUsers(u.getRole())).switchIfEmpty(Mono.defer(() -> {
			log.debug(ApplicationConstant.ROLE_CHECK_FAILED_USER_MANAGEMENT);
			return Mono.error(new AccessDeniedException(ApplicationConstant.REQUIRES_USER_MANAGEMENT_ACCESS));
		}));
	}

	/** SUPER_ADMIN only — create and manage other super administrators. */
	public Mono<CustomUser> requireSuperAdmin() {
		return currentUser().filter(u -> UserRoleHierarchy.isSuperAdmin(u.getRole())).switchIfEmpty(Mono.defer(() -> {
			log.debug(ApplicationConstant.ROLE_CHECK_FAILED_SUPER_ADMIN);
			return Mono.error(new AccessDeniedException(ApplicationConstant.REQUIRES_SUPER_ADMIN));
		}));
	}
}
