export default function StatusBadge({ active, label }) {
  const isActive = active !== false;
  return (
    <span
      className={`inline-flex items-center gap-1.5 rounded-full px-2.5 py-1 text-xs font-medium ring-1 ring-inset ${
        isActive
          ? 'bg-[#fef3ed] text-[#c05a2b] ring-[#c05a2b]/25'
          : 'bg-[#f4f4f4] text-[#888888] ring-[#e0e0e0]'
      }`}
    >
      <span className={`h-1.5 w-1.5 rounded-full ${isActive ? 'bg-[#c05a2b]' : 'bg-[#aaaaaa]'}`} />
      {label ?? (isActive ? 'Active' : 'Inactive')}
    </span>
  );
}
