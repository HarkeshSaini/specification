import { useEffect, useState } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { getNavForRole, getNavSectionsForRole, getSettingsPathForRole } from '../../config/dashboardNav';
import { getInitials } from '../../utils/formatters';
import { getPostLoginPath, getRoleLabel } from '../../utils/roles';
import BrandLogo from '../brand/BrandLogo';
import DashboardBreadcrumb from '../dashboard/DashboardBreadcrumb';
import DashboardIcon from '../dashboard/DashboardIcon';
import DashboardSidebarNav from '../dashboard/DashboardSidebarNav';

const DESKTOP_QUERY = '(min-width: 1024px)';

function readDesktopMatch() {
  if (typeof window === 'undefined') return false;
  return window.matchMedia(DESKTOP_QUERY).matches;
}

function buildBreadcrumbs(pathname, navItems, portalTitle) {
  const current = [...navItems]
    .sort((a, b) => b.to.length - a.to.length)
    .find((item) => pathname === item.to || pathname.startsWith(`${item.to}/`));
  const homePath = navItems[0]?.to?.replace(/\/[^/]+$/, '') ?? '/';
  return [
    { label: 'Home', to: current?.to ?? homePath },
    { label: portalTitle ?? 'Dashboard' },
    { label: current?.label ?? 'Page' },
  ];
}

export default function DashboardLayout({ portalTitle }) {
  const { logout, role, session } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [sidebarOpen, setSidebarOpen] = useState(readDesktopMatch);
  const [isDesktop, setIsDesktop] = useState(readDesktopMatch);

  const navSections = getNavSectionsForRole(role);
  const navItems = getNavForRole(role);
  const currentNav = [...navItems]
    .sort((a, b) => b.to.length - a.to.length)
    .find((item) => location.pathname === item.to || location.pathname.startsWith(`${item.to}/`));
  const pageTitle = currentNav?.label ?? portalTitle ?? 'Dashboard';
  const breadcrumbs = buildBreadcrumbs(location.pathname, navItems, portalTitle);

  useEffect(() => {
    const mq = window.matchMedia(DESKTOP_QUERY);
    const onChange = (event) => {
      setIsDesktop(event.matches);
      setSidebarOpen(event.matches);
    };
    mq.addEventListener('change', onChange);
    return () => mq.removeEventListener('change', onChange);
  }, []);

  useEffect(() => {
    if (sidebarOpen && !isDesktop) {
      const prev = document.body.style.overflow;
      document.body.style.overflow = 'hidden';
      return () => {
        document.body.style.overflow = prev;
      };
    }
    return undefined;
  }, [sidebarOpen, isDesktop]);

  useEffect(() => {
    if (!isDesktop) setSidebarOpen(false);
  }, [location.pathname, isDesktop]);

  function toggleSidebar() {
    setSidebarOpen((open) => !open);
  }

  function closeSidebar() {
    setSidebarOpen(false);
  }

  /** Close sidebar after navigation on mobile/tablet only; keep it open on desktop. */
  function handleSidebarNavigate() {
    if (!isDesktop) {
      closeSidebar();
    }
  }

  function handleLogout() {
    logout();
    navigate('/');
  }

  function handleBack() {
    navigate(-1);
  }

  return (
    <div className={`dashboard-shell min-h-dvh ${sidebarOpen ? 'dashboard-shell-sidebar-open' : ''}`}>
      <div className="dashboard-utility-bar">
        <div className="dashboard-utility-links">
          <span>English</span>
          <button type="button">A+</button>
          <button type="button">A-</button>
          <button type="button">Contrast</button>
        </div>
        <div className="dashboard-utility-links">
          <a href="#main-content">Skip to main content</a>
          <button type="button">Help</button>
        </div>
      </div>

      {sidebarOpen && !isDesktop ? (
        <button type="button" className="dashboard-overlay" onClick={closeSidebar} aria-label="Close menu" />
      ) : null}

      <aside className={`dashboard-sidebar ${sidebarOpen ? 'dashboard-sidebar-open' : ''}`} aria-hidden={!sidebarOpen && !isDesktop}>
        <div className="flex h-full flex-col overflow-hidden">
          <button
            type="button"
            className="dashboard-profile-select"
            aria-label="Profile menu"
            onClick={() => { navigate(getSettingsPathForRole(role)); handleSidebarNavigate(); }}
          >
            <DashboardIcon name="user" className="h-4 w-4 shrink-0 text-[#888]" />
            <span className="flex-1 truncate text-left">{getRoleLabel(role)} profile</span>
            <DashboardIcon name="chevronDown" className="h-4 w-4 shrink-0 text-[#888]" />
          </button>

          <DashboardSidebarNav sections={navSections} portalTitle={portalTitle} onNavigate={handleSidebarNavigate} />

          <div className="dashboard-nav-footer">
            <button type="button" className="dashboard-page-help">
              <DashboardIcon name="help" className="h-4 w-4 shrink-0" />
              <span>Page help</span>
            </button>
            <button type="button" onClick={() => { navigate('/'); handleSidebarNavigate(); }} className="dashboard-nav-item mt-2 w-full rounded border border-[#e0e0e0] bg-white">
              <DashboardIcon name="home" className="h-4 w-4 shrink-0" />
              <span>Public site</span>
            </button>
            <button type="button" onClick={handleLogout} className="dashboard-nav-item mt-1 w-full rounded border border-[#e0e0e0] bg-white text-red-600">
              <span className="flex h-4 w-4 shrink-0 items-center justify-center text-xs font-bold">↪</span>
              <span>Sign out</span>
            </button>
          </div>
        </div>
      </aside>

      <div className="dashboard-main">
        <header className="dashboard-header">
          <div className="flex min-w-0 items-center gap-2 sm:gap-3">
            <button
              type="button"
              className="dashboard-icon-btn dashboard-menu-btn"
              onClick={toggleSidebar}
              aria-label={sidebarOpen ? 'Close menu' : 'Open menu'}
              aria-expanded={sidebarOpen}
            >
              <DashboardIcon name={sidebarOpen ? 'close' : 'menu'} className="h-5 w-5" />
            </button>
            <BrandLogo variant="light" />
          </div>

          <form className="dashboard-header-search" onSubmit={(e) => e.preventDefault()} role="search">
            <select defaultValue="all" aria-label="Search category">
              <option value="all">All</option>
            </select>
            <input type="search" placeholder="Search…" aria-label="Search" />
            <button type="submit" className="dashboard-header-search-btn" aria-label="Search">
              <DashboardIcon name="search" className="h-4 w-4" />
            </button>
          </form>

          <div className="dashboard-header-actions">
            <button
              type="button"
              onClick={() => navigate(getPostLoginPath(role))}
              className="dashboard-notify-btn"
              title="Notifications"
            >
              <DashboardIcon name="bell" className="h-5 w-5" />
              <span className="dashboard-notify-badge">02</span>
            </button>
            <button
              type="button"
              onClick={handleLogout}
              className="dashboard-avatar hidden sm:flex"
              title={getRoleLabel(role)}
            >
              {getInitials(session?.profileId, role)}
            </button>
          </div>
        </header>

        <main id="main-content" className="dashboard-content">
          <div className="dashboard-content-card">
            <DashboardBreadcrumb items={breadcrumbs} />
            <div className="dashboard-page-title-row mb-4">
              <button type="button" className="dashboard-back-btn" onClick={handleBack} aria-label="Go back">
                <DashboardIcon name="arrowLeft" className="h-5 w-5" />
              </button>
              <h1 className="text-xl font-bold text-[#1a1a1a] sm:text-2xl">{pageTitle}</h1>
            </div>
            <Outlet />
          </div>
        </main>
      </div>
    </div>
  );
}
