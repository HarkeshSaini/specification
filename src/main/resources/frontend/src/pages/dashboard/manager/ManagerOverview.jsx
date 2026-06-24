import { useEffect, useState } from 'react';
import ActivityTimeline from '../../../components/dashboard/ActivityTimeline';
import PageHeader from '../../../components/dashboard/PageHeader';
import ProfileSummary from '../../../components/dashboard/ProfileSummary';
import QuickActionGrid from '../../../components/dashboard/QuickActionGrid';
import StatCard from '../../../components/dashboard/StatCard';
import { useAuth } from '../../../context/AuthContext';
import { useDashboardProfile } from '../../../hooks/useDashboardProfile';
import { fetchManagerWebsiteUsers, listWebsiteUsers, unwrapPage } from '../../../services/adminApi';
import { getRoleLabel } from '../../../utils/roles';

export default function ManagerOverview() {
  const { isAdmin } = useAuth();
  const scope = isAdmin ? 'admin' : 'manager';
  const { profile, loading, error } = useDashboardProfile(scope);
  const [userCount, setUserCount] = useState(0);

  useEffect(() => {
    const loader = isAdmin
      ? listWebsiteUsers(0, 1).then((r) => unwrapPage(r).totalElements)
      : fetchManagerWebsiteUsers().then((items) => items.length);
    loader.then(setUserCount).catch(() => {});
  }, [isAdmin]);

  return (
    <div className="dashboard-page space-y-6">
      <PageHeader
        title="Manager Overview"
        description="Track your region, monitor website users, and manage team operations."
      />
      {error ? <div className="rounded-xl border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-300">{error}</div> : null}

      <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
        <StatCard label="Website Users" value={userCount} icon="globe" tone="emerald" loading={loading} />
        <StatCard label="Managed Region" value={profile?.managedRegion || '—'} icon="users" tone="cyan" loading={loading} />
        <StatCard label="Departments" value={profile?.managedDepartments?.length ?? 0} icon="chart" tone="indigo" loading={loading} />
        <StatCard label="Teams" value={profile?.managedTeams?.length ?? 0} icon="shield" tone="violet" loading={loading} />
      </div>

      <QuickActionGrid
        actions={[
          { label: 'View Users', description: 'Browse website user accounts', icon: 'globe', to: '/manager/users' },
          { label: 'Team & Region', description: 'Your management scope', icon: 'users', to: '/manager/team' },
          { label: 'Reports', description: 'Performance insights', icon: 'chart', to: '/manager/reports' },
          { label: 'Settings', description: 'Update your profile', icon: 'settings', to: '/manager/settings' },
        ]}
      />

      <div className="grid gap-6 xl:grid-cols-5">
        <div className="xl:col-span-3">
          <ProfileSummary
            profile={profile}
            roleLabel={isAdmin ? `${getRoleLabel('ADMIN')} (Manager Portal)` : getRoleLabel('MANAGER')}
            extraFields={[
              { label: 'Region', key: 'managedRegion' },
              { label: 'Department', key: 'department' },
            ]}
          />
        </div>
        <div className="xl:col-span-2">
          <h3 className="mb-3 text-sm font-semibold uppercase tracking-widest text-slate-500">Recent Activity</h3>
          <ActivityTimeline
            items={[
              { id: 1, title: 'Dashboard accessed', description: 'Manager portal session started', time: new Date().toISOString(), icon: '✓' },
              { id: 2, title: 'User oversight', description: `${userCount} website users under management`, time: new Date(Date.now() - 7200000).toISOString(), icon: '👥' },
            ]}
          />
        </div>
      </div>
    </div>
  );
}
