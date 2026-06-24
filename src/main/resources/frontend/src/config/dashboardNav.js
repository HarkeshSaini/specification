/**
 * Dashboard sidebar navigation grouped by role.
 * Each section has a label and nav items (to, label, icon).
 */

export const SUPER_ADMIN_NAV_SECTIONS = [
  {
    id: 'main',
    label: 'Dashboard',
    items: [
      { to: '/admin/dashboard', label: 'Overview', icon: 'grid', end: true },
    ],
  },
  {
    id: 'users',
    label: 'User management',
    items: [
      { to: '/admin/users/all', label: 'All users by role', icon: 'users' },
      { to: '/admin/super-admins', label: 'Super administrators', icon: 'shield' },
      { to: '/admin/admins', label: 'Administrators', icon: 'shield' },
      { to: '/admin/managers', label: 'Managers', icon: 'users' },
      { to: '/admin/users', label: 'Website users', icon: 'globe' },
    ],
  },
  {
    id: 'content',
    label: 'Content',
    items: [
      { to: '/admin/blog', label: 'Blog posts', icon: 'edit' },
    ],
  },
  {
    id: 'insights',
    label: 'Insights',
    items: [
      { to: '/admin/analytics', label: 'Analytics', icon: 'chart' },
      { to: '/admin/activity', label: 'Activity log', icon: 'activity' },
    ],
  },
  {
    id: 'access',
    label: 'Access control',
    items: [
      { to: '/admin/roles', label: 'Roles & permissions', icon: 'shield' },
      { to: '/admin/settings', label: 'My settings', icon: 'settings' },
    ],
  },
  {
    id: 'tools',
    label: 'Tools',
    items: [
      { to: '/ai/chat', label: 'AI workspace', icon: 'globe' },
    ],
  },
];

export const ADMIN_NAV_SECTIONS = [
  {
    id: 'main',
    label: 'Dashboard',
    items: [
      { to: '/admin/dashboard', label: 'Overview', icon: 'grid', end: true },
    ],
  },
  {
    id: 'users',
    label: 'User management',
    items: [
      { to: '/admin/users/all', label: 'All users by role', icon: 'users' },
      { to: '/admin/admins', label: 'Administrators', icon: 'shield' },
      { to: '/admin/managers', label: 'Managers', icon: 'users' },
      { to: '/admin/users', label: 'Website users', icon: 'globe' },
    ],
  },
  {
    id: 'content',
    label: 'Content',
    items: [
      { to: '/admin/blog', label: 'Blog posts', icon: 'edit' },
    ],
  },
  {
    id: 'insights',
    label: 'Insights',
    items: [
      { to: '/admin/analytics', label: 'Analytics', icon: 'chart' },
      { to: '/admin/activity', label: 'Activity log', icon: 'activity' },
    ],
  },
  {
    id: 'access',
    label: 'Access control',
    items: [
      { to: '/admin/roles', label: 'Roles & permissions', icon: 'shield' },
      { to: '/admin/settings', label: 'My settings', icon: 'settings' },
    ],
  },
  {
    id: 'tools',
    label: 'Tools',
    items: [
      { to: '/ai/chat', label: 'AI workspace', icon: 'globe' },
    ],
  },
];

export const MANAGER_NAV_SECTIONS = [
  {
    id: 'main',
    label: 'Dashboard',
    items: [
      { to: '/manager/dashboard', label: 'Overview', icon: 'grid', end: true },
    ],
  },
  {
    id: 'users',
    label: 'User management',
    items: [
      { to: '/manager/users', label: 'Website users', icon: 'globe' },
      { to: '/manager/team', label: 'Team & region', icon: 'users' },
    ],
  },
  {
    id: 'insights',
    label: 'Insights',
    items: [
      { to: '/manager/reports', label: 'Reports', icon: 'chart' },
    ],
  },
  {
    id: 'account',
    label: 'Account',
    items: [
      { to: '/manager/settings', label: 'Settings', icon: 'settings' },
      { to: '/ai/chat', label: 'AI workspace', icon: 'globe' },
    ],
  },
];

export const USER_NAV_SECTIONS = [
  {
    id: 'main',
    label: 'Workspace',
    items: [
      { to: '/user/dashboard', label: 'Overview', icon: 'grid', end: true },
      { to: '/user/profile', label: 'My profile', icon: 'user' },
      { to: '/user/preferences', label: 'Preferences', icon: 'sliders' },
      { to: '/user/activity', label: 'Activity', icon: 'activity' },
    ],
  },
  {
    id: 'tools',
    label: 'Tools',
    items: [
      { to: '/ai/chat', label: 'AI workspace', icon: 'globe' },
    ],
  },
];

/** @deprecated Use getNavSectionsForRole — kept for compatibility */
export const ADMIN_NAV = flattenSections(ADMIN_NAV_SECTIONS);
export const MANAGER_NAV = flattenSections(MANAGER_NAV_SECTIONS);
export const USER_NAV = flattenSections(USER_NAV_SECTIONS);

export function flattenSections(sections) {
  return sections.flatMap((section) => section.items);
}

export function getNavSectionsForRole(role) {
  switch (role) {
    case 'SUPER_ADMIN':
      return SUPER_ADMIN_NAV_SECTIONS;
    case 'ADMIN':
      return ADMIN_NAV_SECTIONS;
    case 'MANAGER':
      return MANAGER_NAV_SECTIONS;
    case 'USER':
      return USER_NAV_SECTIONS;
    default:
      return [];
  }
}

export function getNavForRole(role) {
  return flattenSections(getNavSectionsForRole(role));
}

export function getSettingsPathForRole(role) {
  switch (role) {
    case 'SUPER_ADMIN':
    case 'ADMIN':
      return '/admin/settings';
    case 'MANAGER':
      return '/manager/settings';
    case 'USER':
      return '/user/profile';
    default:
      return '/';
  }
}
