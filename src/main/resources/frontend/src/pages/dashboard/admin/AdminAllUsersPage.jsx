import { useEffect, useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import DashboardIcon from '../../../components/dashboard/DashboardIcon';
import PageHeader from '../../../components/dashboard/PageHeader';
import StatusBadge from '../../../components/dashboard/StatusBadge';
import { useAuth } from '../../../context/AuthContext';
import { listAdmins, listManagers, listSuperAdmins, listWebsiteUsers, unwrapPage } from '../../../services/adminApi';
import { formatRelativeDate, getInitials } from '../../../utils/formatters';

const ROLE_TABS = [
  { id: 'all', label: 'All users' },
  { id: 'SUPER_ADMIN', label: 'Super admins' },
  { id: 'ADMIN', label: 'Administrators' },
  { id: 'MANAGER', label: 'Managers' },
  { id: 'USER', label: 'Website users' },
];

const MANAGE_LINKS = {
  SUPER_ADMIN: '/admin/super-admins',
  ADMIN: '/admin/admins',
  MANAGER: '/admin/managers',
  USER: '/admin/users',
};

export default function AdminAllUsersPage() {
  const { isSuperAdmin } = useAuth();
  const [activeTab, setActiveTab] = useState('all');
  const [loading, setLoading] = useState(true);
  const [users, setUsers] = useState([]);

  useEffect(() => {
    setLoading(true);
    const requests = [
      isSuperAdmin ? listSuperAdmins(0, 50).catch(() => ({ data: { items: [] } })) : Promise.resolve({ data: { items: [] } }),
      listAdmins(0, 50),
      listManagers(0, 50),
      listWebsiteUsers(0, 50),
    ];
    Promise.all(requests)
      .then(([s, a, m, u]) => {
        const superAdmins = unwrapPage(s).items.map((item) => ({ ...item, roleType: 'SUPER_ADMIN' }));
        const admins = unwrapPage(a).items.map((item) => ({ ...item, roleType: 'ADMIN' }));
        const managers = unwrapPage(m).items.map((item) => ({ ...item, roleType: 'MANAGER' }));
        const website = unwrapPage(u).items.map((item) => ({ ...item, roleType: 'USER' }));
        setUsers([...superAdmins, ...admins, ...managers, ...website]);
      })
      .catch(() => setUsers([]))
      .finally(() => setLoading(false));
  }, [isSuperAdmin]);

  const filtered = useMemo(() => {
    if (activeTab === 'all') return users;
    return users.filter((u) => u.roleType === activeTab);
  }, [users, activeTab]);

  const counts = useMemo(
    () => ({
      all: users.length,
      SUPER_ADMIN: users.filter((u) => u.roleType === 'SUPER_ADMIN').length,
      ADMIN: users.filter((u) => u.roleType === 'ADMIN').length,
      MANAGER: users.filter((u) => u.roleType === 'MANAGER').length,
      USER: users.filter((u) => u.roleType === 'USER').length,
    }),
    [users],
  );

  return (
    <div className="dashboard-page space-y-6">
      <PageHeader
        description="View every account on the platform grouped by role. Open a role section to add, edit, or deactivate users."
        actions={
          <Link to="/admin/admins" className="btn-gem-primary inline-flex items-center gap-2 rounded px-4 py-2 text-sm font-semibold">
            <DashboardIcon name="plus" className="h-4 w-4" />
            Add user
          </Link>
        }
      />

      <div className="dashboard-role-tabs">
        {ROLE_TABS.map((tab) => (
          <button
            key={tab.id}
            type="button"
            className={`dashboard-role-tab ${activeTab === tab.id ? 'dashboard-role-tab-active' : ''}`}
            onClick={() => setActiveTab(tab.id)}
          >
            {tab.label}
            <span className="dashboard-role-tab-count">{counts[tab.id] ?? 0}</span>
          </button>
        ))}
      </div>

      <div className="dashboard-panel">
        {loading ? (
          <p className="p-8 text-center text-sm text-[#888888]">Loading users…</p>
        ) : filtered.length === 0 ? (
          <p className="p-8 text-center text-sm text-[#888888]">No users found for this role.</p>
        ) : (
          <div className="dashboard-table-wrap">
            <table className="dashboard-table w-full min-w-[640px]">
              <thead>
                <tr>
                  <th>User</th>
                  <th>Role</th>
                  <th>Status</th>
                  <th>Last login</th>
                  <th className="text-right">Manage</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map((user) => (
                  <tr key={user.id}>
                    <td>
                      <div className="flex items-center gap-3">
                        <div className="dashboard-avatar">{getInitials(user.fullName, user.email)}</div>
                        <div>
                          <p className="font-medium text-[#1a1a1a]">{user.fullName || user.email}</p>
                          <p className="text-xs text-[#888888]">{user.email}</p>
                        </div>
                      </div>
                    </td>
                    <td>
                      <span className="dashboard-role-pill">{roleLabel(user.roleType)}</span>
                    </td>
                    <td>
                      <StatusBadge active={user.active !== false} />
                    </td>
                    <td className="text-[#555555]">{formatRelativeDate(user.lastLoginAt)}</td>
                    <td className="text-right">
                      <Link to={MANAGE_LINKS[user.roleType]} className="gem-link text-sm font-medium">
                        Open →
                      </Link>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}

function roleLabel(roleType) {
  const labels = { ADMIN: 'Administrator', MANAGER: 'Manager', USER: 'Website user' };
  return labels[roleType] ?? roleType;
}
