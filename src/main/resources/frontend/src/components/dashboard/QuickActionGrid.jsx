import { Link } from 'react-router-dom';
import DashboardIcon from './DashboardIcon';

export default function QuickActionGrid({ actions = [] }) {
  if (!actions.length) return null;

  return (
    <div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
      {actions.map((action) => {
        const content = (
          <>
            <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-[#fef3ed] text-[#c05a2b] ring-1 ring-[#c05a2b]/20">
              <DashboardIcon name={action.icon} className="h-5 w-5" />
            </div>
            <div className="mt-4">
              <p className="font-semibold text-[#1a1a1a]">{action.label}</p>
              <p className="mt-1 text-xs text-[#888888]">{action.description}</p>
            </div>
          </>
        );

        const className = 'dashboard-action-card group text-left';

        if (action.to) {
          return (
            <Link key={action.label} to={action.to} className={className}>
              {content}
            </Link>
          );
        }

        return (
          <button key={action.label} type="button" onClick={action.onClick} className={className}>
            {content}
          </button>
        );
      })}
    </div>
  );
}
