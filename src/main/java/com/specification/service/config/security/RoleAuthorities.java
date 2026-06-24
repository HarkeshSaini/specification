package com.specification.service.config.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.specification.service.domain.UserRole;

/**
 * Maps domain roles to Spring Security authorities. ADMIN includes manager-level access for website-user administration.
 */
public final class RoleAuthorities {

	private RoleAuthorities() {
	}

	public static List<GrantedAuthority> forRole(UserRole role) {
		return switch (role) {
			case SUPER_ADMIN -> List.of(
					new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"),
					new SimpleGrantedAuthority("ROLE_ADMIN"),
					new SimpleGrantedAuthority("ROLE_MANAGER"),
					new SimpleGrantedAuthority("ROLE_USER"));
			case ADMIN -> List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_MANAGER"),new SimpleGrantedAuthority("ROLE_USER"));
			case MANAGER -> List.of(new SimpleGrantedAuthority("ROLE_MANAGER"),new SimpleGrantedAuthority("ROLE_USER"));
			case USER -> List.of(new SimpleGrantedAuthority("ROLE_USER"));
		};
	}
}
