/**
 * Shared dashboard form controls — consistent GeM-style inputs across admin/manager/user portals.
 */
export function DashboardField({
  label,
  id,
  required = false,
  hint,
  error,
  className = '',
  children,
}) {
  return (
    <div className={`dashboard-field ${className}`.trim()}>
      {label ? (
        <label htmlFor={id} className="dashboard-field-label">
          {label}
          {required ? <span className="dashboard-field-required" aria-hidden="true"> *</span> : null}
        </label>
      ) : null}
      {children}
      {error ? <p className="dashboard-field-error" role="alert">{error}</p> : null}
      {!error && hint ? <p className="dashboard-field-hint">{hint}</p> : null}
    </div>
  );
}

export function DashboardInput({ id, className = '', ...props }) {
  return (
    <input
      id={id}
      className={`dashboard-input ${className}`.trim()}
      {...props}
    />
  );
}

export function DashboardSelect({ id, className = '', children, ...props }) {
  return (
    <select id={id} className={`dashboard-input dashboard-select ${className}`.trim()} {...props}>
      {children}
    </select>
  );
}

export function DashboardTextarea({ id, className = '', ...props }) {
  return (
    <textarea
      id={id}
      className={`dashboard-input dashboard-textarea ${className}`.trim()}
      {...props}
    />
  );
}
