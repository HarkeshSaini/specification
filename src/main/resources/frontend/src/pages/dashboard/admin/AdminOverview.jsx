import { useEffect, useState } from 'react';
import ActivityTimeline from '../../../components/dashboard/ActivityTimeline';
import PageHeader from '../../../components/dashboard/PageHeader';
import ProfileSummary from '../../../components/dashboard/ProfileSummary';
import QuickActionGrid from '../../../components/dashboard/QuickActionGrid';
import StatCard from '../../../components/dashboard/StatCard';
import { useDashboardProfile } from '../../../hooks/useDashboardProfile';
import { listAdmins, listManagers, listWebsiteUsers, unwrapPage } from '../../../services/adminApi';
import { getRoleLabel } from '../../../utils/roles';

export default function AdminOverview() {
  const { profile, loading, error } = useDashboardProfile('admin');
  const [stats, setStats] = useState({ admins: 0, managers: 0, users: 0 });

  useEffect(() => {
    Promise.all([listAdmins(0, 1), listManagers(0, 1), listWebsiteUsers(0, 1)])
      .then(([a, m, u]) => {
        setStats({
          admins: unwrapPage(a).totalElements,
          managers: unwrapPage(m).totalElements,
          users: unwrapPage(u).totalElements,
        });
      })
      .catch(() => {});
  }, []);

  const activity = [
    { id: 1, title: 'Platform ready', description: 'Admin control center initialized', time: new Date().toISOString(), icon: '✓', tone: 'bg-emerald-500/20 text-emerald-400' },
    { id: 2, title: 'User management', description: 'Manage admins, managers, and website users', time: new Date(Date.now() - 3600000).toISOString(), icon: '👥', tone: 'bg-indigo-500/20 text-indigo-400' },
    { id: 3, title: 'Security active', description: 'JWT authentication and role-based access enabled', time: new Date(Date.now() - 86400000).toISOString(), icon: '🔒', tone: 'bg-cyan-500/20 text-cyan-400' },
  ];

  return (
    <div className="dashboard-page space-y-6">
      <PageHeader
        title="Admin Overview"
        description="Monitor platform health, manage users, and configure your organization from one control center."
      />

      {error ? <div className="dashboard-alert dashboard-alert-error">{error}</div> : null}

      <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
        <StatCard label="Administrators" value={stats.admins} icon="shield" tone="indigo" loading={loading} change="Full platform access" />
        <StatCard label="Managers" value={stats.managers} icon="users" tone="cyan" loading={loading} change="Regional oversight" />
        <StatCard label="Website Users" value={stats.users} icon="globe" tone="emerald" loading={loading} change="Public accounts" />
        <StatCard label="Total Accounts" value={stats.admins + stats.managers + stats.users} icon="chart" tone="violet" loading={loading} change="Across all roles" />
      </div>

      <QuickActionGrid
        actions={[
          { label: 'All users', description: 'View every account by role', icon: 'users', to: '/admin/users/all' },
          { label: 'Add Administrator', description: 'Create a new admin account', icon: 'shield', to: '/admin/admins' },
          { label: 'Add Manager', description: 'Assign regional managers', icon: 'users', to: '/admin/managers' },
          { label: 'Add Website User', description: 'Provision public accounts', icon: 'globe', to: '/admin/users' },
          { label: 'Roles & permissions', description: 'View role access matrix', icon: 'shield', to: '/admin/roles' },
          { label: 'View Analytics', description: 'Platform metrics & trends', icon: 'chart', to: '/admin/analytics' },
        ]}
      />

      <div className="grid gap-6 xl:grid-cols-5">
        <div className="xl:col-span-3">
          <ProfileSummary
            profile={profile}
            roleLabel={getRoleLabel('ADMIN')}
            extraFields={[
              { label: 'Department', key: 'department' },
              { label: 'Designation', key: 'designation' },
            ]}
          />
        </div>
        <div className="xl:col-span-2">
          <h3 className="mb-3 text-sm font-semibold uppercase tracking-widest text-[#888888]">Recent Activity</h3>
          <ActivityTimeline items={activity} />
        </div>
      </div>
    </div>
  );
}
