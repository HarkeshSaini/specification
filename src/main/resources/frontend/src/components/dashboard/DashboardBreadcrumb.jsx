import { Link } from 'react-router-dom';

export default function DashboardBreadcrumb({ items = [] }) {
  if (!items.length) return null;

  return (
    <nav className="dashboard-breadcrumb" aria-label="Breadcrumb">
      {items.map((item, index) => {
        const isLast = index === items.length - 1;
        return (
          <span key={item.label} className="inline-flex items-center gap-1.5">
            {index > 0 ? <span className="dashboard-breadcrumb-sep" aria-hidden="true">›</span> : null}
            {item.to && !isLast ? (
              <Link to={item.to}>{item.label}</Link>
            ) : (
              <span aria-current={isLast ? 'page' : undefined}>{item.label}</span>
            )}
          </span>
        );
      })}
    </nav>
  );
}
