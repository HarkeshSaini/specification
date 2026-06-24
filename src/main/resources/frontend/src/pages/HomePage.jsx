import { useState } from 'react';
import { Link } from 'react-router-dom';
import HomeHeroBanner from '../components/home/HomeHeroBanner';
import ListingsSection from '../components/home/ListingsSection';
import { useAuthModal } from '../context/AuthModalContext';
import { STATS, STEPS, TESTIMONIALS } from '../data/homeListings';

function SectionHeading({ eyebrow, title, subtitle }) {
  return (
    <div className="site-section-heading">
      {eyebrow ? <p className="site-section-eyebrow">{eyebrow}</p> : null}
      <h2 className="site-section-title">{title}</h2>
      {subtitle ? <p className="site-section-subtitle">{subtitle}</p> : null}
    </div>
  );
}

export default function HomePage() {
  const { openLogin, openRegister } = useAuthModal();
  const [searchFilter, setSearchFilter] = useState({ query: '', category: 'all' });

  return (
    <div>
      <HomeHeroBanner onSearch={setSearchFilter} />

      <section className="site-stats-bar">
        <div className="site-stats-inner">
          {STATS.map((stat) => (
            <div key={stat.label} className="site-stat">
              <p className="site-stat-value">{stat.value}</p>
              <p className="site-stat-label">{stat.label}</p>
            </div>
          ))}
        </div>
      </section>

      <ListingsSection searchFilter={searchFilter} />

      <section id="how-it-works" className="site-section scroll-mt-28">
        <div className="site-section-inner">
          <SectionHeading eyebrow="Process" title="How it works" subtitle="Three simple steps from login to delivery." />
          <div className="site-steps-grid">
            {STEPS.map((step) => (
              <div key={step.step} className="site-step-card">
                <span className="site-step-num">{step.step}</span>
                <h3 className="site-step-title">{step.title}</h3>
                <p className="site-step-text">{step.text}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="site-section">
        <div className="site-section-inner">
          <div className="site-ai-banner">
            <div className="site-ai-content">
              <p className="site-section-eyebrow">AI powered</p>
              <h2 className="site-ai-title">Intelligent agents for your codebase</h2>
              <p className="site-ai-desc">
                Scan files, build context, and apply changes with Ollama-backed AI — directly from your workspace.
              </p>
              <Link to="/ai/chat" className="btn-gradient site-ai-btn">
                Open AI workspace
              </Link>
            </div>
            <div className="site-ai-terminal">
              {['Scan file →', 'Build context →', 'LLM plan →', 'Apply changes ✓'].map((line) => (
                <div key={line} className="site-ai-line">
                  {line}
                </div>
              ))}
            </div>
          </div>
        </div>
      </section>

      <section className="site-section">
        <div className="site-section-inner">
          <SectionHeading eyebrow="Testimonials" title="Loved by engineering teams" />
          <div className="site-testimonials-grid">
            {TESTIMONIALS.map((t) => (
              <blockquote key={t.author} className="site-testimonial-card">
                <p className="site-testimonial-quote">&ldquo;{t.quote}&rdquo;</p>
                <footer className="site-testimonial-footer">
                  <p className="site-testimonial-author">{t.author}</p>
                  <p className="site-testimonial-role">{t.role}</p>
                </footer>
              </blockquote>
            ))}
          </div>
        </div>
      </section>

      <section className="site-section site-section-cta">
        <div className="site-section-inner">
          <div className="site-cta-banner">
            <h2 className="site-cta-title">Ready to get started?</h2>
            <p className="site-cta-desc">
              Create an account or sign in to access your specification workspace.
            </p>
            <div className="site-cta-actions">
              <button type="button" onClick={openRegister} className="site-cta-btn-primary">
                Create account
              </button>
              <button type="button" onClick={openLogin} className="site-cta-btn-secondary">
                Login
              </button>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
