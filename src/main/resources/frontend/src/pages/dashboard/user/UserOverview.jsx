import ActivityTimeline from '../../../components/dashboard/ActivityTimeline';
import PageHeader from '../../../components/dashboard/PageHeader';
import ProfileSummary from '../../../components/dashboard/ProfileSummary';
import QuickActionGrid from '../../../components/dashboard/QuickActionGrid';
import StatCard from '../../../components/dashboard/StatCard';
import { useDashboardProfile } from '../../../hooks/useDashboardProfile';
import { getRoleLabel } from '../../../utils/roles';

export default function UserOverview() {
  const { profile, loading, error } = useDashboardProfile('user');

  return (
    <div className="dashboard-page space-y-6">
      <PageHeader
        title={`Welcome back${profile?.fullName ? `, ${profile.fullName.split(' ')[0]}` : ''}`}
        description="Your personal workspace on the Specification Platform."
      />
      {error ? <div className="rounded-xl border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-300">{error}</div> : null}

      <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
        <StatCard label="Account Status" value={profile?.active !== false ? 'Active' : 'Inactive'} icon="shield" tone="emerald" loading={loading} />
        <StatCard label="Language" value={profile?.language || 'en'} icon="globe" tone="cyan" loading={loading} />
        <StatCard label="Theme" value={profile?.theme || 'dark'} icon="sliders" tone="indigo" loading={loading} />
        <StatCard label="Timezone" value={profile?.timezone || 'UTC'} icon="activity" tone="violet" loading={loading} />
      </div>

      <QuickActionGrid
        actions={[
          { label: 'My Profile', description: 'View account details', icon: 'user', to: '/user/profile' },
          { label: 'Preferences', description: 'Language, theme & timezone', icon: 'sliders', to: '/user/preferences' },
          { label: 'Activity', description: 'Your recent actions', icon: 'activity', to: '/user/activity' },
          { label: 'Explore AI', description: 'Try the AI assistant', icon: 'chart', to: '/ai' },
        ]}
      />

      <div className="grid gap-6 xl:grid-cols-5">
        <div className="xl:col-span-3">
          <ProfileSummary
            profile={profile}
            roleLabel={getRoleLabel('USER')}
            extraFields={[
              { label: 'Language', key: 'language' },
              { label: 'Timezone', key: 'timezone' },
            ]}
          />
        </div>
        <div className="xl:col-span-2">
          <h3 className="mb-3 text-sm font-semibold uppercase tracking-widest text-slate-500">Recent Activity</h3>
          <ActivityTimeline
            items={[
              { id: 1, title: 'Signed in', description: 'Dashboard session started', time: new Date().toISOString(), icon: '✓' },
              { id: 2, title: 'Profile loaded', description: profile?.email ?? 'Account verified', time: new Date(Date.now() - 60000).toISOString(), icon: '👤' },
            ]}
          />
        </div>
      </div>
    </div>
  );
}
