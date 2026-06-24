export const ROLES = {
  SUPER_ADMIN: 'SUPER_ADMIN',
  ADMIN: 'ADMIN',
  MANAGER: 'MANAGER',
  WEBSITE_USER: 'USER',
};

export function isSuperAdmin(role) {
  return role === ROLES.SUPER_ADMIN;
}

export function isAdminPortalRole(role) {
  return role === ROLES.SUPER_ADMIN || role === ROLES.ADMIN || role === ROLES.MANAGER;
}

export function canManageUsers(role) {
  return role === ROLES.SUPER_ADMIN || role === ROLES.ADMIN;
}

export function isAdmin(role) {
  return role === ROLES.ADMIN;
}

export function isManager(role) {
  return role === ROLES.MANAGER;
}

export function isWebsiteUser(role) {
  return role === ROLES.WEBSITE_USER;
}

export function getPostLoginPath(role, redirectPath) {
  if (redirectPath) return redirectPath;
  switch (role) {
    case ROLES.SUPER_ADMIN:
    case ROLES.ADMIN:
      return '/admin/dashboard';
    case ROLES.MANAGER:
      return '/manager/dashboard';
    case ROLES.WEBSITE_USER:
      return '/user/dashboard';
    default:
      return '/';
  }
}

export function getRoleLabel(role) {
  const labels = {
    SUPER_ADMIN: 'Super Admin',
    ADMIN: 'Admin',
    MANAGER: 'Manager',
    USER: 'Website User',
    WEBSITE_USER: 'Website User',
  };
  return labels[role] ?? role;
}
