import { useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuthModal } from '../../context/AuthModalContext';
import { LISTINGS, STATS } from '../../data/homeListings';

const CATEGORIES = [
  { value: 'all', label: 'All' },
  { value: 'documentation', label: 'Documentation' },
  { value: 'ai', label: 'AI' },
  { value: 'collaboration', label: 'Collaboration' },
  { value: 'enterprise', label: 'Enterprise' },
  { value: 'cms', label: 'CMS' },
  { value: 'insights', label: 'Insights' },
];

const QUICK_TAGS = ['API Specs', 'AI Assistant', 'Team Workspace', 'Analytics', 'Security'];

const HIGHLIGHTS = [
  { icon: '📋', title: 'Unified specs', text: 'OpenAPI, versioning & change history in one hub.' },
  { icon: '🤖', title: 'AI-powered', text: 'Scan, refactor and document with intelligent agents.' },
  { icon: '🔒', title: 'Secure by default', text: 'JWT auth, role-based access & audit trails.' },
];

export default function HomeHeroBanner({ onSearch }) {
  const { openLogin, openRegister } = useAuthModal();
  const [category, setCategory] = useState('all');
  const [query, setQuery] = useState('');
  const [activeTag, setActiveTag] = useState('');

  function applySearch(nextQuery = query, nextCategory = category) {
    onSearch?.({ query: nextQuery.trim(), category: nextCategory });
  }

  function scrollToListings() {
    document.getElementById('listings-section')?.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }

  function handleSubmit(e) {
    e.preventDefault();
    applySearch();
    scrollToListings();
  }

  function handleReset() {
    setQuery('');
    setCategory('all');
    setActiveTag('');
    onSearch?.({ query: '', category: 'all' });
  }

  function handleTagClick(tag) {
    setQuery(tag);
    setCategory('all');
    setActiveTag(tag);
    onSearch?.({ query: tag, category: 'all' });
    scrollToListings();
  }

  function handlePreviewClick(item) {
    setQuery(item.title);
    setCategory(item.category.toLowerCase());
    setActiveTag('');
    onSearch?.({ query: item.title, category: item.category.toLowerCase() });
    scrollToListings();
  }

  const previewItems = LISTINGS.slice(0, 3);

  return (
    <section className="home-hero">
      <div className="home-hero-bg" aria-hidden="true">
        <div className="home-hero-orb home-hero-orb-1" />
        <div className="home-hero-orb home-hero-orb-2" />
        <div className="home-hero-grid" />
      </div>

      <div className="home-hero-inner">
        <div className="home-hero-frame">
          <div className="home-hero-split">
            {/* Row 1 left — Search */}
            <div className="home-hero-search animate-fade-up">
              <div className="home-hero-search-panel">
                <div className="home-hero-search-header">
                  <div className="home-hero-search-icon-wrap">
                    <svg className="home-hero-search-icon" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5} aria-hidden="true">
                      <path strokeLinecap="round" strokeLinejoin="round" d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z" />
                    </svg>
                  </div>
                  <div>
                    <p className="home-hero-search-label">Search the platform</p>
                    <h2 className="home-hero-search-title">Find specs, tools &amp; services</h2>
                  </div>
                </div>

                <p className="home-hero-search-desc">
                  Browse modules, documentation &amp; AI tools across your workspace.
                </p>

                <form onSubmit={handleSubmit} className="home-hero-search-form" role="search">
                  <label htmlFor="hero-category" className="home-hero-field-label">
                    Category <span className="home-hero-required">*</span>
                  </label>
                  <div className="home-hero-search-bar">
                    <select
                      id="hero-category"
                      value={category}
                      onChange={(e) => setCategory(e.target.value)}
                      className="home-hero-search-select"
                      aria-label="Search category"
                    >
                      {CATEGORIES.map((c) => (
                        <option key={c.value} value={c.value}>
                          {c.label}
                        </option>
                      ))}
                    </select>
                    <input
                      type="search"
                      value={query}
                      onChange={(e) => {
                        setQuery(e.target.value);
                        setActiveTag('');
                      }}
                      placeholder="Search specifications, modules, AI tools…"
                      className="home-hero-search-input"
                      aria-label="Search keywords"
                    />
                    <button type="submit" className="home-hero-search-btn" aria-label="Search">
                      <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2.5}>
                        <path strokeLinecap="round" strokeLinejoin="round" d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z" />
                      </svg>
                    </button>
                  </div>

                  <div className="home-hero-search-actions">
                    <button type="button" onClick={handleReset} className="home-hero-btn-reset">
                      Reset
                    </button>
                    <button type="submit" className="home-hero-btn-submit">
                      Search
                    </button>
                  </div>
                </form>

                <div className="home-hero-tags">
                  <span className="home-hero-tags-label">Popular:</span>
                  <div className="home-hero-tags-list">
                    {QUICK_TAGS.map((tag) => (
                      <button
                        key={tag}
                        type="button"
                        onClick={() => handleTagClick(tag)}
                        className={`home-hero-tag ${activeTag === tag ? 'home-hero-tag-active' : ''}`}
                      >
                        {tag}
                      </button>
                    ))}
                  </div>
                </div>

                <div className="home-hero-search-stats">
                  {STATS.slice(0, 3).map((stat, i) => (
                    <div key={stat.label} className="home-hero-search-stat-wrap">
                      {i > 0 ? <div className="home-hero-search-stat-divider" aria-hidden="true" /> : null}
                      <div className="home-hero-search-stat">
                        <span className="home-hero-search-stat-value">{stat.value}</span>
                        <span className="home-hero-search-stat-label">{stat.label}</span>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>

            {/* Row 1 right — Headline & CTAs */}
            <div className="home-hero-intro animate-fade-up-delay-1">
              <span className="home-hero-badge">
                <span className="home-hero-badge-dot" />
                Next-gen specification platform
              </span>

              <h1 className="home-hero-headline">
                Design specs that
                <span className="home-hero-headline-accent"> teams actually use</span>
              </h1>

              <p className="home-hero-subtitle">
                Banner, listings, AI tools, and secure login — everything your website needs in one
                beautiful, modern experience built for engineering teams.
              </p>

              <div className="home-hero-actions">
                <button type="button" onClick={openRegister} className="btn-gradient home-hero-btn-primary">
                  Create free account
                </button>
                <button type="button" onClick={openLogin} className="home-hero-btn-secondary">
                  Sign in
                </button>
                <Link to="/ai/chat" className="home-hero-btn-ghost">
                  Explore AI →
                </Link>
              </div>
            </div>

            {/* Row 2 left — Feature highlights (fills space below search) */}
            <div className="home-hero-highlights">
              {HIGHLIGHTS.map((item) => (
                <div key={item.title} className="home-hero-highlight">
                  <span className="home-hero-highlight-icon" aria-hidden="true">{item.icon}</span>
                  <div>
                    <p className="home-hero-highlight-title">{item.title}</p>
                    <p className="home-hero-highlight-text">{item.text}</p>
                  </div>
                </div>
              ))}
            </div>

            {/* Row 2 right — Featured listings preview */}
            <div className="home-hero-showcase">
              <div className="home-hero-showcase-glow" aria-hidden="true" />
              <div className="home-hero-preview">
                <div className="home-hero-preview-header">
                  <span className="home-hero-preview-dot home-hero-preview-dot-red" />
                  <span className="home-hero-preview-dot home-hero-preview-dot-amber" />
                  <span className="home-hero-preview-dot home-hero-preview-dot-green" />
                  <span className="home-hero-preview-title">Featured listings</span>
                  <span className="home-hero-preview-live">Live</span>
                </div>
                <div className="home-hero-preview-list">
                  {previewItems.map((item) => (
                    <button
                      key={item.id}
                      type="button"
                      onClick={() => handlePreviewClick(item)}
                      className="home-hero-preview-row"
                    >
                      <div className="home-hero-preview-row-main">
                        <span className="home-hero-preview-tag">{item.tag}</span>
                        <p className="home-hero-preview-row-title">{item.title}</p>
                        <p className="home-hero-preview-row-cat">{item.category}</p>
                      </div>
                      <span className="home-hero-preview-arrow" aria-hidden="true">→</span>
                    </button>
                  ))}
                </div>
              </div>

              <div className="home-hero-float-card home-hero-float-card-1" aria-hidden="true">
                <span className="home-hero-float-icon">✓</span>
                <div>
                  <p className="home-hero-float-title">98% satisfaction</p>
                  <p className="home-hero-float-text">Team adoption rate</p>
                </div>
              </div>

              <div className="home-hero-float-card home-hero-float-card-2" aria-hidden="true">
                <span className="home-hero-float-icon home-hero-float-icon-ai">AI</span>
                <div>
                  <p className="home-hero-float-title">Smart agents</p>
                  <p className="home-hero-float-text">Ollama-powered</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
