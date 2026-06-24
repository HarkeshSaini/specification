import UserManagementPanel from '../../../components/dashboard/UserManagementPanel';
import { deleteAdmin, listAdmins } from '../../../services/adminApi';

export default function AdminUsersPage() {
  return (
    <UserManagementPanel
      title="Administrators"
      description="Manage platform administrators with full system access and elevated permissions."
      entityLabel="Administrator"
      listPath="/admin/admins"
      listFn={listAdmins}
      deleteFn={deleteAdmin}
      extraColumns={[
        { key: 'department', label: 'Department' },
        { key: 'designation', label: 'Designation' },
      ]}
    />
  );
}
