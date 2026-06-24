package com.specification.service.domain;

import com.specification.service.config.security.RoleAuthorities;

/**
 * Application roles. {@link RoleAuthorities} maps these to Spring authorities
 * (including inherited roles so ADMIN can access manager-level secured paths).
 */
public enum UserRole {
	SUPER_ADMIN,
	ADMIN,
	MANAGER,
	USER
}
