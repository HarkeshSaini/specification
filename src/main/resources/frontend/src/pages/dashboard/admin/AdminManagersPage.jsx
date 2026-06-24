import UserManagementPanel from '../../../components/dashboard/UserManagementPanel';
import { deleteManager, listManagers } from '../../../services/adminApi';

export default function AdminManagersPage() {
  return (
    <UserManagementPanel
      title="Managers"
      description="Create and manage regional managers who oversee teams and website users."
      entityLabel="Manager"
      listPath="/admin/managers"
      listFn={listManagers}
      deleteFn={deleteManager}
      extraColumns={[
        { key: 'managedRegion', label: 'Region' },
        { key: 'department', label: 'Department' },
      ]}
    />
  );
}
