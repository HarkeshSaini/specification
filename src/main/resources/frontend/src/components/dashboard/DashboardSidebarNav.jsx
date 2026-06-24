import { NavLink } from 'react-router-dom';
import DashboardIcon from './DashboardIcon';

function navClass({ isActive }) {
  return `dashboard-nav-item ${isActive ? 'dashboard-nav-item-active' : ''}`;
}

export default function DashboardSidebarNav({ sections, portalTitle, onNavigate }) {
  return (
    <nav className="flex-1 overflow-y-auto overscroll-contain py-2" aria-label="Dashboard navigation">
      {sections.map((section, index) => (
        <div key={section.id} className={`dashboard-nav-card ${index > 0 ? 'dashboard-nav-card-spaced' : ''}`}>
          <div className={`dashboard-nav-section ${index === 0 ? 'dashboard-nav-section-active' : 'dashboard-nav-section-label'}`}>
            {index === 0 ? portalTitle : section.label}
          </div>
          {section.items.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              end={item.end}
              className={navClass}
              onClick={onNavigate}
            >
              <DashboardIcon name={item.icon} className="h-4 w-4 shrink-0" />
              <span className="truncate">{item.label}</span>
            </NavLink>
          ))}
        </div>
      ))}
    </nav>
  );
}
