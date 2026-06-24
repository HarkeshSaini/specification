package com.specification.service.domain;

/**
 * Role hierarchy and capability checks (single place for authorization rules).
 */
public final class UserRoleHierarchy {

	private UserRoleHierarchy() {
	}

	public static boolean isSuperAdmin(UserRole role) {
		return role == UserRole.SUPER_ADMIN;
	}

	public static boolean canManageUsers(UserRole role) {
		return role == UserRole.SUPER_ADMIN || role == UserRole.ADMIN;
	}

	public static boolean isAtLeast(UserRole actual, UserRole required) {
		return rank(actual) >= rank(required);
	}

	private static int rank(UserRole role) {
		return switch (role) {
			case SUPER_ADMIN -> 4;
			case ADMIN -> 3;
			case MANAGER -> 2;
			case USER -> 1;
		};
	}
}
