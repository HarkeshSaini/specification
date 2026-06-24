package com.specification.service.config.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.specification.service.domain.UserRole;

import lombok.Getter;

/**
 * Authenticated principal after JWT validation (password not used for request authorization).
 */

@Getter
public class CustomUser implements UserDetails {

	private final String authAccountId;
	private final String profileId;
	private final String email;
	private final UserRole role;
	private final Collection<? extends GrantedAuthority> authorities;

	public CustomUser(String authAccountId, String profileId, String email, UserRole role, Collection<? extends GrantedAuthority> authorities) {
		this.authAccountId = authAccountId;
		this.profileId = profileId;
		this.email = email;
		this.role = role;
		this.authorities = authorities;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public String getPassword() {
		return "";
	}

}
