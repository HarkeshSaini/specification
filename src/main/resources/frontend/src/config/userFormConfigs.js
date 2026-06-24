import {
  createAdmin,
  createManager,
  createSuperAdmin,
  createWebsiteUser,
  getAdminById,
  getManagerById,
  getSuperAdminById,
  getWebsiteUserById,
  updateAdmin,
  updateManager,
  updateSuperAdmin,
  updateWebsiteUser,
} from '../services/adminApi';

export const SUPER_ADMIN_FORM_CONFIG = {
  entityLabel: 'Super Administrator',
  listPath: '/admin/super-admins',
  formFields: ['department', 'designation', 'allAccess'],
  createFn: createSuperAdmin,
  updateFn: updateSuperAdmin,
  getByIdFn: getSuperAdminById,
};

export const ADMIN_FORM_CONFIG = {
  entityLabel: 'Administrator',
  listPath: '/admin/admins',
  formFields: ['department', 'designation'],
  createFn: createAdmin,
  updateFn: updateAdmin,
  getByIdFn: getAdminById,
};

export const MANAGER_FORM_CONFIG = {
  entityLabel: 'Manager',
  listPath: '/admin/managers',
  formFields: ['department', 'designation', 'managedRegion'],
  createFn: createManager,
  updateFn: updateManager,
  getByIdFn: getManagerById,
};

export const WEBSITE_USER_FORM_CONFIG = {
  entityLabel: 'Website User',
  listPath: '/admin/users',
  formFields: ['language'],
  createFn: createWebsiteUser,
  updateFn: updateWebsiteUser,
  getByIdFn: getWebsiteUserById,
};
