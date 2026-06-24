import PageHeader from '../../../components/dashboard/PageHeader';
import ProfileSummary from '../../../components/dashboard/ProfileSummary';
import { useAuth } from '../../../context/AuthContext';
import { useDashboardProfile } from '../../../hooks/useDashboardProfile';
import { getRoleLabel } from '../../../utils/roles';

export default function ManagerSettingsPage() {
  const { isAdmin } = useAuth();
  const { profile, error } = useDashboardProfile(isAdmin ? 'admin' : 'manager');

  return (
    <div className="dashboard-page space-y-6">
      <PageHeader title="Settings" description="Your manager profile and account details." />
      {error ? <div className="rounded-xl border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-300">{error}</div> : null}

      <ProfileSummary
        profile={profile}
        roleLabel={getRoleLabel(isAdmin ? 'ADMIN' : 'MANAGER')}
        extraFields={[
          { label: 'Region', key: 'managedRegion' },
          { label: 'Department', key: 'department' },
          { label: 'Reporting Director', key: 'reportingDirector' },
        ]}
      />
    </div>
  );
}
