import { useEffect, useState } from 'react';
import PageHeader from '../../../components/dashboard/PageHeader';
import StatCard from '../../../components/dashboard/StatCard';
import { useAuth } from '../../../context/AuthContext';
import { fetchManagerWebsiteUsers, listWebsiteUsers, unwrapPage } from '../../../services/adminApi';

export default function ManagerReportsPage() {
  const { isAdmin } = useAuth();
  const [stats, setStats] = useState({ total: 0, active: 0, inactive: 0 });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loader = isAdmin
      ? listWebsiteUsers(0, 100).then((r) => unwrapPage(r).items)
      : fetchManagerWebsiteUsers();
    loader
      .then((items) => {
        const active = items.filter((i) => i.active !== false).length;
        setStats({ total: items.length, active, inactive: items.length - active });
      })
      .finally(() => setLoading(false));
  }, [isAdmin]);

  return (
    <div className="dashboard-page space-y-6">
      <PageHeader title="Reports" description="User engagement and account status reports for your region." />

      <div className="grid gap-4 sm:grid-cols-3">
        <StatCard label="Total Users" value={stats.total} icon="globe" tone="indigo" loading={loading} />
        <StatCard label="Active" value={stats.active} icon="users" tone="emerald" loading={loading} />
        <StatCard label="Inactive" value={stats.inactive} icon="chart" tone="amber" loading={loading} />
      </div>

      <div className="dashboard-panel p-6">
        <h3 className="text-lg font-semibold text-white">Engagement Summary</h3>
        <div className="mt-6 grid gap-4 sm:grid-cols-2">
          <MetricBar label="Active rate" value={stats.total ? Math.round((stats.active / stats.total) * 100) : 0} color="bg-emerald-500" />
          <MetricBar label="Inactive rate" value={stats.total ? Math.round((stats.inactive / stats.total) * 100) : 0} color="bg-amber-500" />
        </div>
        <p className="mt-6 text-sm text-slate-400">
          Detailed analytics with charts, export, and scheduled reports will be available as the platform scales.
        </p>
      </div>
    </div>
  );
}

function MetricBar({ label, value, color }) {
  return (
    <div>
      <div className="mb-2 flex justify-between text-sm">
        <span className="text-slate-400">{label}</span>
        <span className="font-medium text-white">{value}%</span>
      </div>
      <div className="h-3 overflow-hidden rounded-full bg-white/5">
        <div className={`h-full rounded-full transition-all duration-700 ${color}`} style={{ width: `${value}%` }} />
      </div>
    </div>
  );
}
