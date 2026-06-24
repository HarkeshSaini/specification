import DashboardIcon from './DashboardIcon';

export default function EmptyState({ icon = 'users', title, description, action }) {
  return (
    <div className="dashboard-empty-state">
      <div className="mx-auto flex h-16 w-16 items-center justify-center rounded-lg bg-[#fef3ed] text-[#c05a2b] ring-1 ring-[#c05a2b]/20">
        <DashboardIcon name={icon} className="h-8 w-8" />
      </div>
      <h3 className="mt-5 text-lg font-semibold text-[#1a1a1a]">{title}</h3>
      {description ? <p className="mt-2 max-w-sm text-sm text-[#888888]">{description}</p> : null}
      {action ? <div className="mt-6">{action}</div> : null}
    </div>
  );
}
