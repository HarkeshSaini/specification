import { useState } from 'react';
import { Link } from 'react-router-dom';
import { FOOTER_COLUMNS, FOOTER_SOCIAL } from '../../config/siteNav';
import BrandLogo from '../brand/BrandLogo';

function FooterSubscribeBar() {
  const [email, setEmail] = useState('');
  const [status, setStatus] = useState('idle');

  function handleSubmit(e) {
    e.preventDefault();
    const trimmed = email.trim();
    if (!trimmed || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(trimmed)) {
      setStatus('error');
      return;
    }
    setStatus('success');
    setEmail('');
  }

  return (
    <div className="site-footer-subscribe-bar">
      <div className="site-footer-subscribe-bar-inner">
        <div className="site-footer-subscribe-bar-text">
          <h3 className="site-footer-subscribe-bar-title">Subscribe to our newsletter</h3>
          <p className="site-footer-subscribe-bar-desc">
            Product updates, release notes &amp; platform insights — straight to your inbox.
          </p>
        </div>

        <form onSubmit={handleSubmit} className="site-footer-subscribe-bar-form" noValidate>
          <label htmlFor="footer-subscribe-email" className="sr-only">
            Email address
          </label>
          <div className="site-footer-subscribe-bar-row">
            <input
              id="footer-subscribe-email"
              type="email"
              value={email}
              onChange={(e) => {
                setEmail(e.target.value);
                if (status !== 'idle') setStatus('idle');
              }}
              placeholder="Enter your email address"
              className="site-footer-subscribe-bar-input"
              autoComplete="email"
              required
            />
            <button type="submit" className="site-footer-subscribe-bar-btn">
              Subscribe
            </button>
          </div>
          {status === 'success' ? (
            <p className="site-footer-subscribe-bar-msg site-footer-subscribe-bar-msg-success" role="status">
              Thank you! You&apos;re subscribed.
            </p>
          ) : null}
          {status === 'error' ? (
            <p className="site-footer-subscribe-bar-msg site-footer-subscribe-bar-msg-error" role="alert">
              Please enter a valid email address.
            </p>
          ) : null}
        </form>
      </div>
    </div>
  );
}

export default function Footer() {
  return (
    <footer className="site-footer">
      <FooterSubscribeBar />

      <div className="site-footer-top">
        <div className="site-footer-inner">
          <div className="site-footer-brand">
            <BrandLogo variant="light" />
            <p className="site-footer-desc">
              Specification Platform helps teams design, document, and deliver software with clarity.
              Built for modern engineering workflows and government-grade portals.
            </p>
            <div className="site-footer-contact">
              <p>
                <span className="site-footer-contact-label">Email</span>
                <a href="mailto:support@specification.platform">support@specification.platform</a>
              </p>
              <p>
                <span className="site-footer-contact-label">Phone</span>
                <a href="tel:+911800123456">1800-123-456 (Toll Free)</a>
              </p>
            </div>
            <div className="site-footer-social">
              {FOOTER_SOCIAL.map((item) => (
                <a key={item.label} href={item.href} target="_blank" rel="noopener noreferrer" className="site-social-link">
                  {item.label}
                </a>
              ))}
            </div>
          </div>

          <div className="site-footer-links">
            {FOOTER_COLUMNS.map((col) => (
              <div key={col.title} className="site-footer-col">
                <h3 className="site-footer-col-title">{col.title}</h3>
                <ul className="site-footer-col-list">
                  {col.links.map((link) => (
                    <li key={link.label}>
                      <Link to={link.to} className="site-footer-link">
                        {link.label}
                      </Link>
                    </li>
                  ))}
                </ul>
              </div>
            ))}
          </div>
        </div>
      </div>

      <div className="site-footer-bottom">
        <div className="site-footer-bottom-inner">
          <p className="site-footer-copy">
            © {new Date().getFullYear()} Specification Platform. All rights reserved.
          </p>
          <div className="site-footer-bottom-links">
            <Link to="/contact">Privacy Policy</Link>
            <Link to="/contact">Terms of Service</Link>
            <Link to="/contact">Cookie Policy</Link>
            <Link to="/contact">Accessibility</Link>
            <Link to="/contact">Sitemap</Link>
          </div>
          <p className="site-footer-version">Version 1.0 · Last updated Jun 2026</p>
        </div>
      </div>
    </footer>
  );
}
