export default function Pagination({ page, totalPages, totalElements, onPageChange, loading, pageSize = 10 }) {
  if (totalPages <= 1 && totalElements <= pageSize) return null;

  const pages = [];
  const start = Math.max(0, page - 2);
  const end = Math.min(totalPages - 1, page + 2);
  for (let i = start; i <= end; i += 1) pages.push(i);

  return (
    <div className="dashboard-pagination">
      <p className="text-[#888888]">
        {loading ? 'Loading…' : `${totalElements} total records`}
      </p>
      <div className="flex flex-wrap items-center gap-2">
        <button
          type="button"
          disabled={page <= 0 || loading}
          onClick={() => onPageChange(page - 1)}
          className="dashboard-btn-ghost rounded px-3 py-1.5 text-xs disabled:opacity-40"
        >
          Previous
        </button>
        {pages.map((p) => (
          <button
            key={p}
            type="button"
            disabled={loading}
            onClick={() => onPageChange(p)}
            className={`dashboard-pagination-page ${p === page ? 'dashboard-pagination-page-active' : ''}`}
          >
            {p + 1}
          </button>
        ))}
        <button
          type="button"
          disabled={page >= totalPages - 1 || loading}
          onClick={() => onPageChange(page + 1)}
          className="dashboard-btn-ghost rounded px-3 py-1.5 text-xs disabled:opacity-40"
        >
          Next
        </button>
        <span className="ml-2 hidden text-[#888888] sm:inline">View</span>
        <select
          defaultValue={pageSize}
          disabled
          className="hidden rounded border border-[#e0e0e0] bg-white px-2 py-1 text-xs text-[#555555] sm:inline"
          aria-label="Rows per page"
        >
          <option value={10}>10 rows</option>
        </select>
      </div>
    </div>
  );
}
