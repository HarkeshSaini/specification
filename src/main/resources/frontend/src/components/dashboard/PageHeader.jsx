export default function PageHeader({ title, description, actions }) {
  if (!title && !description && !actions) return null;

  return (
    <div className="dashboard-page-header mb-5">
      <div className="min-w-0 flex-1">
        {title ? <h2 className="text-lg font-semibold text-[#1a1a1a] sm:text-xl">{title}</h2> : null}
        {description ? <p className={`max-w-2xl text-sm text-[#555555] ${title ? 'mt-1' : ''}`}>{description}</p> : null}
      </div>
      {actions ? <div className="flex shrink-0 flex-wrap items-center gap-2">{actions}</div> : null}
    </div>
  );
}
