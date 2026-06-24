import { formatRelativeDate } from '../../utils/formatters';

export default function ActivityTimeline({ items = [] }) {
  if (!items.length) {
    return (
      <div className="dashboard-panel p-6">
        <p className="text-sm text-[#888888]">No recent activity to display.</p>
      </div>
    );
  }

  return (
    <div className="dashboard-panel divide-y divide-[#eeeeee]">
      {items.map((item, index) => (
        <div key={item.id ?? index} className="flex gap-4 px-5 py-4">
          <div className="relative flex flex-col items-center">
            <div className="z-10 flex h-8 w-8 items-center justify-center rounded-full bg-[#fef3ed] text-[#c05a2b] ring-4 ring-white">
              <span className="text-xs font-bold">{item.icon ?? '•'}</span>
            </div>
            {index < items.length - 1 ? <div className="mt-1 w-px flex-1 bg-[#e0e0e0]" /> : null}
          </div>
          <div className="min-w-0 flex-1 pb-2">
            <p className="text-sm font-medium text-[#1a1a1a]">{item.title}</p>
            <p className="mt-0.5 text-sm text-[#555555]">{item.description}</p>
            <p className="mt-1.5 text-xs text-[#888888]">{formatRelativeDate(item.time)}</p>
          </div>
        </div>
      ))}
    </div>
  );
}
