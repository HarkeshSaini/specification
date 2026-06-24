import DashboardIcon from './DashboardIcon';

const TONE_STYLES = {
  orange: 'bg-[#fef3ed] text-[#c05a2b] ring-[#c05a2b]/20',
  indigo: 'bg-[#fef3ed] text-[#c05a2b] ring-[#c05a2b]/20',
  cyan: 'bg-[#e3f2fd] text-[#1565c0] ring-[#1565c0]/20',
  emerald: 'bg-[#e8f5e9] text-[#2e7d32] ring-[#2e7d32]/20',
  amber: 'bg-[#fff8e1] text-[#f57f17] ring-[#f57f17]/20',
  rose: 'bg-[#fce4ec] text-[#c62828] ring-[#c62828]/20',
  violet: 'bg-[#f3e5f5] text-[#7b1fa2] ring-[#7b1fa2]/20',
};

export default function StatCard({ label, value, change, icon, tone = 'orange', loading }) {
  const toneClass = TONE_STYLES[tone] ?? TONE_STYLES.orange;

  return (
    <div className="dashboard-stat-card">
      <div className="flex items-start justify-between gap-4">
        <div className="min-w-0 flex-1">
          <p className="text-xs font-semibold uppercase tracking-wider text-[#888888]">{label}</p>
          <p className="mt-2 text-3xl font-bold tracking-tight text-[#1a1a1a]">
            {loading ? <span className="inline-block h-8 w-16 animate-pulse rounded bg-[#f4f4f4]" /> : value}
          </p>
          {change ? <p className="mt-2 text-xs text-[#888888]">{change}</p> : null}
        </div>
        {icon ? (
          <div className={`flex h-11 w-11 shrink-0 items-center justify-center rounded-lg ring-1 ${toneClass}`}>
            <DashboardIcon name={icon} className="h-5 w-5" />
          </div>
        ) : null}
      </div>
    </div>
  );
}
