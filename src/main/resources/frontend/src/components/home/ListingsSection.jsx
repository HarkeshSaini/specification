import { useMemo, useState } from 'react';
import { useAuthModal } from '../../context/AuthModalContext';
import { LISTING_CATEGORIES, LISTINGS } from '../../data/homeListings';

export default function ListingsSection({ searchFilter = { query: '', category: 'all' } }) {
  const { openLogin } = useAuthModal();
  const [localCategory, setLocalCategory] = useState('all');

  const activeCategory = searchFilter.category !== 'all' ? searchFilter.category : localCategory;

  const filteredListings = useMemo(() => {
    let results = LISTINGS;
    const cat = searchFilter.category !== 'all' ? searchFilter.category : localCategory;
    if (cat && cat !== 'all') {
      results = results.filter((item) => item.category.toLowerCase() === cat);
    }
    if (searchFilter.query) {
      const q = searchFilter.query.toLowerCase();
      results = results.filter(
        (item) =>
          item.title.toLowerCase().includes(q) ||
          item.description.toLowerCase().includes(q) ||
          item.category.toLowerCase().includes(q) ||
          item.tag.toLowerCase().includes(q) ||
          item.features?.some((f) => f.toLowerCase().includes(q)),
      );
    }
    return results;
  }, [searchFilter, localCategory]);

  const categoryCounts = useMemo(() => {
    const counts = { all: LISTINGS.length };
    LISTINGS.forEach((item) => {
      const key = item.category.toLowerCase();
      counts[key] = (counts[key] || 0) + 1;
    });
    return counts;
  }, []);

  const hasActiveFilter = searchFilter.query || searchFilter.category !== 'all' || localCategory !== 'all';

  function handleCategoryClick(catId) {
    setLocalCategory(catId);
  }

  function clearFilters() {
    setLocalCategory('all');
  }

  return (
    <section id="listings-section" className="listings-section scroll-mt-28">
      <div className="listings-section-inner">
        <div className="listings-section-header">
          <div>
            <p className="listings-section-eyebrow">Website Listings</p>
            <h2 className="listings-section-title">Everything your platform offers</h2>
            <p className="listings-section-subtitle">
              {hasActiveFilter
                ? `Showing ${filteredListings.length} of ${LISTINGS.length} services matching your criteria.`
                : `Browse ${LISTINGS.length} modules and services available on the Specification platform.`}
            </p>
          </div>
          <div className="listings-section-meta">
            <span className="listings-meta-badge">
              <span className="listings-meta-dot" />
              {LISTINGS.filter((l) => l.active).length} Active
            </span>
            <span className="listings-meta-text">Last updated Jun 2026</span>
          </div>
        </div>

        <div className="listings-mobile-categories">
          <div className="listings-mobile-categories-scroll">
            {LISTING_CATEGORIES.map((cat) => {
              const isActive = activeCategory === cat.id;
              const count = categoryCounts[cat.id] ?? 0;
              return (
                <button
                  key={cat.id}
                  type="button"
                  onClick={() => handleCategoryClick(cat.id)}
                  className={`listings-mobile-cat ${isActive ? 'listings-mobile-cat-active' : ''}`}
                >
                  {cat.label}
                  <span className="listings-mobile-cat-count">{count}</span>
                </button>
              );
            })}
          </div>
        </div>

        <div className="listings-layout">
          <aside className="listings-sidebar">
            <div className="listings-sidebar-card">
              <h3 className="listings-sidebar-title">Categories</h3>
              <ul className="listings-category-list">
                {LISTING_CATEGORIES.map((cat) => {
                  const isActive = activeCategory === cat.id;
                  const count = categoryCounts[cat.id] ?? 0;
                  return (
                    <li key={cat.id}>
                      <button
                        type="button"
                        onClick={() => handleCategoryClick(cat.id)}
                        className={`listings-category-btn ${isActive ? 'listings-category-btn-active' : ''}`}
                      >
                        <span>{cat.label}</span>
                        <span className="listings-category-count">{count}</span>
                      </button>
                    </li>
                  );
                })}
              </ul>
            </div>

            <div className="listings-sidebar-card listings-sidebar-promo">
              <div className="listings-promo-icon" aria-hidden="true">🚀</div>
              <h3 className="listings-promo-title">Need a custom module?</h3>
              <p className="listings-promo-text">
                Contact our team to add bespoke services and integrations to your portal.
              </p>
              <button type="button" onClick={openLogin} className="listings-promo-btn">
                Get started
              </button>
            </div>

            <div className="listings-sidebar-card">
              <h3 className="listings-sidebar-title">Quick stats</h3>
              <dl className="listings-quick-stats">
                <div className="listings-quick-stat">
                  <dt>Total services</dt>
                  <dd>{LISTINGS.length}</dd>
                </div>
                <div className="listings-quick-stat">
                  <dt>Categories</dt>
                  <dd>{LISTING_CATEGORIES.length - 1}</dd>
                </div>
                <div className="listings-quick-stat">
                  <dt>Enterprise ready</dt>
                  <dd>Yes</dd>
                </div>
              </dl>
            </div>
          </aside>

          <div className="listings-main">
            <div className="listings-toolbar">
              <div className="listings-toolbar-left">
                <span className="listings-result-count">
                  <strong>{filteredListings.length}</strong> listing{filteredListings.length === 1 ? '' : 's'}
                </span>
                {hasActiveFilter ? (
                  <button type="button" onClick={clearFilters} className="listings-clear-btn">
                    Clear filters
                  </button>
                ) : null}
              </div>
              <div className="listings-toolbar-right">
                <span className="listings-view-label">View</span>
                <select className="listings-view-select" defaultValue="grid" aria-label="View mode">
                  <option value="grid">Grid view</option>
                  <option value="list">List view</option>
                </select>
              </div>
            </div>

            {filteredListings.length === 0 ? (
              <div className="listings-empty">
                <div className="listings-empty-icon" aria-hidden="true">🔍</div>
                <h3 className="listings-empty-title">No listings found</h3>
                <p className="listings-empty-text">
                  Try adjusting your search keywords or selecting a different category.
                </p>
                <button type="button" onClick={clearFilters} className="btn-gem-primary listings-empty-btn">
                  Show all listings
                </button>
              </div>
            ) : (
              <div className="listings-grid">
                {filteredListings.map((item) => (
                  <article key={item.id} className="listing-card">
                    <div className="listing-card-top">
                      <div className="listing-card-icon" aria-hidden="true">{item.icon}</div>
                      <div className="listing-card-badges">
                        <span className="listing-tag">{item.tag}</span>
                        {item.active ? (
                          <span className="listing-status">
                            <span className="listing-status-dot" />
                            Active
                          </span>
                        ) : null}
                      </div>
                    </div>

                    <p className="listing-category">{item.category}</p>
                    <h3 className="listing-title">{item.title}</h3>
                    <p className="listing-desc">{item.description}</p>

                    {item.features?.length ? (
                      <ul className="listing-features">
                        {item.features.map((feat) => (
                          <li key={feat}>
                            <span className="listing-feature-check" aria-hidden="true">✓</span>
                            {feat}
                          </li>
                        ))}
                      </ul>
                    ) : null}

                    <div className="listing-card-footer">
                      <span className="listing-updated">Updated {item.updated}</span>
                      <button type="button" onClick={openLogin} className="listing-cta">
                        Learn more →
                      </button>
                    </div>
                  </article>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </section>
  );
}
