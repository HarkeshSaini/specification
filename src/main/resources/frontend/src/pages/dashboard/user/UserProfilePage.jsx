import PageHeader from '../../../components/dashboard/PageHeader';
import ProfileSummary from '../../../components/dashboard/ProfileSummary';
import { useDashboardProfile } from '../../../hooks/useDashboardProfile';
import { formatDate } from '../../../utils/formatters';
import { getRoleLabel } from '../../../utils/roles';

export default function UserProfilePage() {
  const { profile, error } = useDashboardProfile('user');

  return (
    <div className="dashboard-page space-y-6">
      <PageHeader title="My Profile" description="Your account information and contact details." />
      {error ? <div className="rounded-xl border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-300">{error}</div> : null}

      <ProfileSummary profile={profile} roleLabel={getRoleLabel('USER')} />

      {profile ? (
        <div className="grid gap-6 lg:grid-cols-2">
          <DetailCard title="Contact Information">
            <DetailRow label="Email" value={profile.email} />
            <DetailRow label="Username" value={profile.username} />
            <DetailRow label="Phone" value={profile.phoneNumber} />
          </DetailCard>
          <DetailCard title="Account Details">
            <DetailRow label="Full name" value={profile.fullName} />
            <DetailRow label="Created" value={formatDate(profile.createdAt)} />
            <DetailRow label="Last updated" value={formatDate(profile.updatedAt)} />
            <DetailRow label="Last login" value={formatDate(profile.lastLoginAt)} />
          </DetailCard>
          {profile.address ? (
            <DetailCard title="Address" className="lg:col-span-2">
              <DetailRow label="Line 1" value={profile.address.addressLine1} />
              <DetailRow label="City" value={profile.address.city} />
              <DetailRow label="State" value={profile.address.state} />
              <DetailRow label="Country" value={profile.address.country} />
              <DetailRow label="Postal code" value={profile.address.postalCode} />
            </DetailCard>
          ) : null}
        </div>
      ) : null}
    </div>
  );
}

function DetailCard({ title, children, className = '' }) {
  return (
    <div className={`dashboard-panel p-6 ${className}`}>
      <h3 className="text-lg font-semibold text-white">{title}</h3>
      <dl className="mt-4 space-y-3">{children}</dl>
    </div>
  );
}

function DetailRow({ label, value }) {
  return (
    <div className="flex justify-between gap-4 border-b border-white/5 pb-3 last:border-0 last:pb-0">
      <dt className="text-sm text-slate-500">{label}</dt>
      <dd className="text-right text-sm font-medium text-slate-300">{value || '—'}</dd>
    </div>
  );
}
