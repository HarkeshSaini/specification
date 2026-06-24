import ActivityTimeline from '../../../components/dashboard/ActivityTimeline';
import PageHeader from '../../../components/dashboard/PageHeader';
import { useDashboardProfile } from '../../../hooks/useDashboardProfile';
import { formatDate } from '../../../utils/formatters';

export default function UserActivityPage() {
  const { profile, error } = useDashboardProfile('user');

  const activities = [
    { id: 1, title: 'Dashboard visit', description: 'Accessed user dashboard', time: new Date().toISOString(), icon: '📊', tone: 'bg-indigo-500/20 text-indigo-400' },
    { id: 2, title: 'Session active', description: 'Authenticated session in progress', time: new Date(Date.now() - 300000).toISOString(), icon: '🔐', tone: 'bg-emerald-500/20 text-emerald-400' },
  ];

  if (profile?.lastLoginAt) {
    activities.push({
      id: 3,
      title: 'Previous login',
      description: `Last signed in on ${formatDate(profile.lastLoginAt)}`,
      time: profile.lastLoginAt,
      icon: '👤',
      tone: 'bg-cyan-500/20 text-cyan-400',
    });
  }

  if (profile?.createdAt) {
    activities.push({
      id: 4,
      title: 'Account created',
      description: 'Welcome to Specification Platform',
      time: profile.createdAt,
      icon: '🎉',
      tone: 'bg-violet-500/20 text-violet-400',
    });
  }

  return (
    <div className="dashboard-page space-y-6">
      <PageHeader title="Activity" description="Your recent actions and account history." />
      {error ? <div className="rounded-xl border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-300">{error}</div> : null}
      <ActivityTimeline items={activities} />
    </div>
  );
}
