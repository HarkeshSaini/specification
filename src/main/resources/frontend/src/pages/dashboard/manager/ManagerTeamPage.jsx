import PageHeader from '../../../components/dashboard/PageHeader';
import ProfileSummary from '../../../components/dashboard/ProfileSummary';
import { useAuth } from '../../../context/AuthContext';
import { useDashboardProfile } from '../../../hooks/useDashboardProfile';
import { getRoleLabel } from '../../../utils/roles';

export default function ManagerTeamPage() {
  const { isAdmin } = useAuth();
  const { profile, error } = useDashboardProfile(isAdmin ? 'admin' : 'manager');

  return (
    <div className="dashboard-page space-y-6">
      <PageHeader title="Team & Region" description="Your management scope, departments, and team assignments." />
      {error ? <div className="rounded-xl border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-300">{error}</div> : null}

      <ProfileSummary
        profile={profile}
        roleLabel={getRoleLabel(isAdmin ? 'ADMIN' : 'MANAGER')}
        extraFields={[
          { label: 'Managed Region', key: 'managedRegion' },
          { label: 'Department', key: 'department' },
          { label: 'Designation', key: 'designation' },
        ]}
      />

      <div className="grid gap-6 lg:grid-cols-2">
        <ListPanel title="Managed Departments" items={profile?.managedDepartments} emptyText="No departments assigned yet." />
        <ListPanel title="Managed Teams" items={profile?.managedTeams} emptyText="No teams assigned yet." />
      </div>
    </div>
  );
}

function ListPanel({ title, items, emptyText }) {
  return (
    <div className="dashboard-panel p-6">
      <h3 className="text-lg font-semibold text-white">{title}</h3>
      {items?.length ? (
        <ul className="mt-4 space-y-2">
          {items.map((item) => (
            <li key={item} className="flex items-center gap-3 rounded-xl border border-white/5 bg-white/[0.02] px-4 py-3">
              <span className="flex h-8 w-8 items-center justify-center rounded-lg bg-cyan-500/10 text-sm font-bold text-cyan-400">
                {item.charAt(0).toUpperCase()}
              </span>
              <span className="text-sm text-slate-300">{item}</span>
            </li>
          ))}
        </ul>
      ) : (
        <p className="mt-4 text-sm text-slate-500">{emptyText}</p>
      )}
    </div>
  );
}
