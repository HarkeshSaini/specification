import UserManagementPanel from '../../../components/dashboard/UserManagementPanel';
import { deleteWebsiteUser, listWebsiteUsers } from '../../../services/adminApi';

export default function AdminWebsiteUsersPage() {
  return (
    <UserManagementPanel
      title="Website Users"
      description="Manage public-facing user accounts, preferences, and access to platform features."
      entityLabel="Website User"
      listPath="/admin/users"
      listFn={listWebsiteUsers}
      deleteFn={deleteWebsiteUser}
      extraColumns={[
        { key: 'language', label: 'Language' },
        { key: 'theme', label: 'Theme' },
      ]}
    />
  );
}
