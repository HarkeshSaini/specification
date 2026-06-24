import { useEffect, useState } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { HEADER_ACTIONS, HEADER_NAV, UTILITY_LINKS } from '../../config/siteNav';
import { useAuth } from '../../context/AuthContext';
import { useAuthModal } from '../../context/AuthModalContext';
import { getPostLoginPath } from '../../utils/roles';
import BrandLogo from '../brand/BrandLogo';
import ThemeToggle from '../theme/ThemeToggle';

function navClass({ isActive }) {
  return `site-nav-link ${isActive ? 'site-nav-link-active' : ''}`;
}

function HeaderNavLink({ link, onClick }) {
  if (link.hash) {
    return (
      <a href={link.to} className="site-nav-link" onClick={onClick}>
        {link.label}
      </a>
    );
  }
  return (
    <NavLink to={link.to} end={link.end} className={navClass} onClick={onClick}>
      {link.label}
    </NavLink>
  );
}

export default function Header() {
  const [menuOpen, setMenuOpen] = useState(false);
  const { isAuthenticated, logout, session, role, redirectPath } = useAuth();
  const { openLogin, openRegister } = useAuthModal();
  const navigate = useNavigate();

  function closeMenu() {
    setMenuOpen(false);
  }

  useEffect(() => {
    if (!menuOpen) return undefined;
    const prev = document.body.style.overflow;
    document.body.style.overflow = 'hidden';
    return () => {
      document.body.style.overflow = prev;
    };
  }, [menuOpen]);

  function handleLoginClick() {
    closeMenu();
    if (isAuthenticated) {
      navigate(getPostLoginPath(role, redirectPath));
      return;
    }
    openLogin();
  }

  function handleLogout() {
    logout();
    closeMenu();
    navigate('/');
  }

  return (
    <header className="site-header">
      <div className="site-utility-bar">
        <div className="site-utility-group">
          {UTILITY_LINKS.map((item) =>
            item.type === 'button' ? (
              <button key={item.label} type="button">
                {item.label}
              </button>
            ) : (
              <span key={item.label}>{item.label}</span>
            ),
          )}
        </div>
        <div className="site-utility-group">
          <a href="#listings-section">Skip to listings</a>
          <button type="button" onClick={() => navigate('/contact')}>
            Help
          </button>
        </div>
      </div>

      <div className="site-header-main">
        <div className="site-header-inner">
          <BrandLogo variant="light" compactOnMobile />

          <div className="site-header-right">
            <nav className="site-nav" aria-label="Main navigation">
              {HEADER_NAV.map((link) => (
                <HeaderNavLink key={link.label} link={link} onClick={closeMenu} />
              ))}
              {HEADER_ACTIONS.map((link) => (
                <NavLink key={link.label} to={link.to} className={navClass} onClick={closeMenu}>
                  {link.label}
                </NavLink>
              ))}
            </nav>

            <div className="site-header-divider" aria-hidden="true" />

            <div className="site-header-actions">
              <ThemeToggle />
              {isAuthenticated ? (
                <>
                  <button
                    type="button"
                    onClick={() => navigate(getPostLoginPath(role, redirectPath))}
                    className="site-btn-dashboard"
                  >
                    {session?.role ?? 'Member'} Dashboard
                  </button>
                  <button type="button" onClick={handleLogout} className="site-btn-outline">
                    Logout
                  </button>
                </>
              ) : (
                <>
                  <button type="button" onClick={openRegister} className="site-btn-outline">
                    Register
                  </button>
                  <button type="button" onClick={openLogin} className="btn-gradient site-btn-login">
                    Login
                  </button>
                </>
              )}
            </div>

            <button
              type="button"
              className="site-menu-toggle"
              onClick={() => setMenuOpen((v) => !v)}
              aria-label="Toggle menu"
              aria-expanded={menuOpen}
            >
              <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                {menuOpen ? (
                  <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
                ) : (
                  <path strokeLinecap="round" strokeLinejoin="round" d="M4 6h16M4 12h16M4 18h16" />
                )}
              </svg>
            </button>
          </div>
        </div>
      </div>

      {menuOpen ? (
        <div className="site-mobile-menu">
          <nav className="site-mobile-nav" aria-label="Mobile navigation">
            {HEADER_NAV.map((link) => (
              <HeaderNavLink key={link.label} link={link} onClick={closeMenu} />
            ))}
            {HEADER_ACTIONS.map((link) => (
              <NavLink key={link.label} to={link.to} className={navClass} onClick={closeMenu}>
                {link.label}
              </NavLink>
            ))}
            <div className="site-mobile-actions">
              <div className="site-mobile-theme">
                <span className="site-mobile-theme-label">Theme</span>
                <ThemeToggle />
              </div>
              {isAuthenticated ? (
                <>
                  <button type="button" onClick={handleLoginClick} className="btn-gradient site-btn-login w-full">
                    Go to Dashboard
                  </button>
                  <button type="button" onClick={handleLogout} className="site-btn-outline w-full">
                    Logout
                  </button>
                </>
              ) : (
                <>
                  <button type="button" onClick={() => { openRegister(); closeMenu(); }} className="site-btn-outline w-full">
                    Register
                  </button>
                  <button type="button" onClick={handleLoginClick} className="btn-gradient site-btn-login w-full">
                    Login
                  </button>
                </>
              )}
            </div>
          </nav>
        </div>
      ) : null}
    </header>
  );
}
