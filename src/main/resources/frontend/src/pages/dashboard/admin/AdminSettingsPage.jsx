import PageHeader from '../../../components/dashboard/PageHeader';
import ProfileSummary from '../../../components/dashboard/ProfileSummary';
import { useDashboardProfile } from '../../../hooks/useDashboardProfile';
import { getRoleLabel } from '../../../utils/roles';

export default function AdminSettingsPage() {
  const { profile, error } = useDashboardProfile('admin');

  return (
    <div className="dashboard-page space-y-6">
      <PageHeader description="Your admin profile and account configuration." />
      {error ? (
        <div className="dashboard-alert dashboard-alert-error">{error}</div>
      ) : null}

      <ProfileSummary
        profile={profile}
        roleLabel={getRoleLabel('ADMIN')}
        extraFields={[
          { label: 'Department', key: 'department' },
          { label: 'Designation', key: 'designation' },
          { label: 'Reporting Manager', key: 'reportingManager' },
        ]}
      />

      <div className="grid gap-6 lg:grid-cols-2">
        <SettingsSection title="Permissions" description="Your admin capabilities">
          <TagList items={profile?.permissions?.length ? profile.permissions : ['Full platform access', 'User management', 'System configuration']} />
        </SettingsSection>
        <SettingsSection title="Roles" description="Assigned admin roles">
          <TagList items={profile?.roles?.length ? profile.roles : ['ADMIN']} tone="orange" />
        </SettingsSection>
      </div>
    </div>
  );
}

function SettingsSection({ title, description, children }) {
  return (
    <div className="dashboard-panel p-6">
      <h3 className="text-lg font-semibold text-[#1a1a1a]">{title}</h3>
      <p className="mt-1 text-sm text-[#888888]">{description}</p>
      <div className="mt-4">{children}</div>
    </div>
  );
}

function TagList({ items, tone = 'neutral' }) {
  const colors = {
    orange: 'bg-[#fef3ed] text-[#c05a2b] ring-[#c05a2b]/20',
    neutral: 'bg-[#f4f4f4] text-[#555555] ring-[#e0e0e0]',
  };
  return (
    <div className="flex flex-wrap gap-2">
      {items.map((item) => (
        <span key={item} className={`rounded-full px-3 py-1 text-xs font-medium ring-1 ring-inset ${colors[tone] ?? colors.neutral}`}>
          {item}
        </span>
      ))}
    </div>
  );
}
