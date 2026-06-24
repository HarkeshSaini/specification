import UserFormPage from '../../../components/dashboard/UserFormPage';
import {
  ADMIN_FORM_CONFIG,
  MANAGER_FORM_CONFIG,
  SUPER_ADMIN_FORM_CONFIG,
  WEBSITE_USER_FORM_CONFIG,
} from '../../../config/userFormConfigs';

export function SuperAdminUserFormPage() {
  return <UserFormPage config={SUPER_ADMIN_FORM_CONFIG} />;
}

export function AdminUserFormPage() {
  return <UserFormPage config={ADMIN_FORM_CONFIG} />;
}

export function ManagerUserFormPage() {
  return <UserFormPage config={MANAGER_FORM_CONFIG} />;
}

export function WebsiteUserFormPage() {
  return <UserFormPage config={WEBSITE_USER_FORM_CONFIG} />;
}
