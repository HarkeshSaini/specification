import { getInitials, formatDate } from '../../utils/formatters';
import StatusBadge from './StatusBadge';

export default function ProfileSummary({ profile, roleLabel, extraFields = [] }) {
  if (!profile) {
    return (
      <div className="dashboard-panel p-6">
        <div className="animate-pulse space-y-4">
          <div className="flex items-center gap-4">
            <div className="h-16 w-16 rounded-full bg-[#f4f4f4]" />
            <div className="space-y-2">
              <div className="h-5 w-40 rounded bg-[#f4f4f4]" />
              <div className="h-4 w-56 rounded bg-[#f4f4f4]" />
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="dashboard-panel overflow-hidden">
      <div className="dashboard-profile-banner" />
      <div className="relative px-6 pb-6 pt-0">
        <div className="-mt-10 flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
          <div className="flex items-end gap-4">
            <div className="dashboard-avatar dashboard-avatar-lg ring-4 ring-white">
              {getInitials(profile.fullName, profile.email)}
            </div>
            <div className="pb-1">
              <div className="flex flex-wrap items-center gap-2">
                <h2 className="text-xl font-bold text-[#1a1a1a]">{profile.fullName || profile.email}</h2>
                <StatusBadge active={profile.active !== false} />
              </div>
              <p className="text-sm text-[#555555]">{profile.email}</p>
              {roleLabel ? <p className="mt-0.5 text-xs font-medium uppercase tracking-wider text-[#c05a2b]">{roleLabel}</p> : null}
            </div>
          </div>
          <div className="text-right text-xs text-[#888888]">
            <p>Member since {formatDate(profile.createdAt)}</p>
            <p>Last login {formatDate(profile.lastLoginAt)}</p>
          </div>
        </div>

        <div className="mt-6 grid gap-3 sm:grid-cols-2 lg:grid-cols-4">
          <InfoTile label="Username" value={profile.username || '—'} />
          <InfoTile label="Phone" value={profile.phoneNumber || '—'} />
          {extraFields.map((field) => (
            <InfoTile key={field.label} label={field.label} value={field.value ?? profile[field.key] ?? '—'} />
          ))}
        </div>
      </div>
    </div>
  );
}

function InfoTile({ label, value }) {
  return (
    <div className="rounded border border-[#e0e0e0] bg-[#fafafa] px-4 py-3">
      <p className="text-xs font-medium uppercase tracking-wider text-[#888888]">{label}</p>
      <p className="mt-1 truncate text-sm font-medium text-[#1a1a1a]">{value}</p>
    </div>
  );
}
