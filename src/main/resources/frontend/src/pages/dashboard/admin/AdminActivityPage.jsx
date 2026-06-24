import ActivityTimeline from '../../../components/dashboard/ActivityTimeline';
import PageHeader from '../../../components/dashboard/PageHeader';

const ACTIVITY = [
  { id: 1, title: 'Admin signed in', description: 'Successful authentication via JWT', time: new Date().toISOString(), icon: '✓' },
  { id: 2, title: 'User management', description: 'Administrators, managers, and website users can be managed from the sidebar', time: new Date(Date.now() - 7200000).toISOString(), icon: '👥' },
  { id: 3, title: 'Role-based access', description: 'API routes enforce ADMIN, MANAGER, and USER permissions', time: new Date(Date.now() - 86400000).toISOString(), icon: '🔒' },
  { id: 4, title: 'Soft delete enabled', description: 'Deactivated accounts retain audit history', time: new Date(Date.now() - 172800000).toISOString(), icon: '📋' },
];

export default function AdminActivityPage() {
  return (
    <div className="dashboard-page space-y-6">
      <PageHeader description="Recent platform events and administrative actions across all roles." />

      <div className="grid gap-6 lg:grid-cols-3">
        <div className="lg:col-span-2">
          <h3 className="dashboard-section-title">Recent activity</h3>
          <ActivityTimeline items={ACTIVITY} />
        </div>

        <div className="dashboard-panel p-5">
          <h3 className="text-base font-semibold text-[#1a1a1a]">Quick filters</h3>
          <ul className="mt-4 space-y-2 text-sm text-[#555555]">
            <li className="dashboard-insight-row">All roles</li>
            <li className="dashboard-insight-row">Administrators only</li>
            <li className="dashboard-insight-row">Managers only</li>
            <li className="dashboard-insight-row">Website users only</li>
          </ul>
          <p className="mt-4 text-xs text-[#888888]">
            Full audit streaming can be connected to your logging backend in a future release.
          </p>
        </div>
      </div>
    </div>
  );
}
