import { useEffect, useState } from 'react';
import PageHeader from '../../../components/dashboard/PageHeader';
import StatCard from '../../../components/dashboard/StatCard';
import { listAdmins, listManagers, listWebsiteUsers, unwrapPage } from '../../../services/adminApi';

export default function AdminAnalyticsPage() {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([listAdmins(0, 100), listManagers(0, 100), listWebsiteUsers(0, 100)])
      .then(([a, m, u]) => {
        const admins = unwrapPage(a);
        const managers = unwrapPage(m);
        const users = unwrapPage(u);
        const allItems = [...admins.items, ...managers.items, ...users.items];
        const active = allItems.filter((i) => i.active !== false).length;
        setStats({
          total: admins.totalElements + managers.totalElements + users.totalElements,
          active,
          inactive: allItems.length - active,
          admins: admins.totalElements,
          managers: managers.totalElements,
          users: users.totalElements,
        });
      })
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="dashboard-page space-y-6">
      <PageHeader description="Platform-wide metrics and user distribution across all roles." />

      <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
        <StatCard label="Total Users" value={stats?.total ?? 0} icon="users" tone="indigo" loading={loading} />
        <StatCard label="Active Accounts" value={stats?.active ?? 0} icon="shield" tone="emerald" loading={loading} />
        <StatCard label="Inactive Accounts" value={stats?.inactive ?? 0} icon="chart" tone="amber" loading={loading} />
        <StatCard label="Growth Ready" value="100%" icon="globe" tone="cyan" loading={loading} change="Scalable architecture" />
      </div>

      <div className="grid gap-6 lg:grid-cols-2">
        <DistributionCard
          title="Role Distribution"
          items={[
            { label: 'Administrators', value: stats?.admins ?? 0, color: 'bg-[#c05a2b]' },
            { label: 'Managers', value: stats?.managers ?? 0, color: 'bg-[#1565c0]' },
            { label: 'Website Users', value: stats?.users ?? 0, color: 'bg-[#2e7d32]' },
          ]}
          total={stats?.total ?? 1}
          loading={loading}
        />

        <div className="dashboard-panel p-6">
          <h3 className="text-lg font-semibold text-[#1a1a1a]">Platform Insights</h3>
          <ul className="mt-4 space-y-3 text-sm text-[#555555]">
            <li className="flex items-start gap-2">
              <span className="text-[#c05a2b]">•</span>
              Role-based access control is active across all API endpoints
            </li>
            <li className="flex items-start gap-2">
              <span className="text-[#1565c0]">•</span>
              MongoDB reactive backend supports horizontal scaling
            </li>
            <li className="flex items-start gap-2">
              <span className="text-[#2e7d32]">•</span>
              JWT sessions with configurable expiry for secure authentication
            </li>
            <li className="flex items-start gap-2">
              <span className="text-[#7b1fa2]">•</span>
              Soft-delete preserves audit trail for deactivated accounts
            </li>
          </ul>
        </div>
      </div>
    </div>
  );
}

function DistributionCard({ title, items, total, loading }) {
  return (
    <div className="dashboard-panel p-6">
      <h3 className="text-lg font-semibold text-[#1a1a1a]">{title}</h3>
      <div className="mt-6 space-y-5">
        {items.map((item) => {
          const pct = total > 0 ? Math.round((item.value / total) * 100) : 0;
          return (
            <div key={item.label}>
              <div className="mb-2 flex justify-between text-sm">
                <span className="text-[#555555]">{item.label}</span>
                <span className="font-medium text-[#1a1a1a]">{loading ? '…' : `${item.value} (${pct}%)`}</span>
              </div>
              <div className="h-2 overflow-hidden rounded-full bg-[#f4f4f4]">
                <div
                  className={`h-full rounded-full transition-all duration-700 ${item.color}`}
                  style={{ width: loading ? '0%' : `${pct}%` }}
                />
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
