import UserManagementPanel from '../../../components/dashboard/UserManagementPanel';
import { deleteSuperAdmin, listSuperAdmins } from '../../../services/adminApi';

export default function SuperAdminUsersPage() {
  return (
    <UserManagementPanel
      title="Super Administrators"
      description="Manage super administrators with full access to all user types and platform operations."
      entityLabel="Super Administrator"
      listPath="/admin/super-admins"
      listFn={listSuperAdmins}
      deleteFn={deleteSuperAdmin}
      extraColumns={[
        { key: 'department', label: 'Department' },
        { key: 'designation', label: 'Designation' },
        { key: 'allAccess', label: 'All access', render: (item) => (item.allAccess === false ? 'No' : 'Yes') },
      ]}
    />
  );
}
