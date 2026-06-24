export default function ContactPage() {
  return (
    <div className="site-page">
      <div className="site-page-inner site-page-split">
        <div className="site-page-intro">
          <p className="site-section-eyebrow">Contact Us</p>
          <h1 className="site-page-title">Let&apos;s talk</h1>
          <p className="site-page-lead">
            Questions about specifications, AI features, or enterprise plans? We&apos;re here to help.
          </p>
          <ul className="site-contact-list">
            <li>📧 support@specification.io</li>
            <li>📍 Global · Remote-first team</li>
            <li>🕐 Mon–Fri · 9am–6pm UTC</li>
          </ul>
        </div>
        <form className="site-form-card" onSubmit={(e) => e.preventDefault()}>
          <div className="site-form-field">
            <label htmlFor="contact-name">Name</label>
            <input id="contact-name" required className="site-form-input" />
          </div>
          <div className="site-form-field">
            <label htmlFor="contact-email">Email</label>
            <input id="contact-email" type="email" required className="site-form-input" />
          </div>
          <div className="site-form-field">
            <label htmlFor="contact-msg">Message</label>
            <textarea id="contact-msg" rows={5} required className="site-form-input site-form-textarea" />
          </div>
          <button type="submit" className="btn-gradient site-form-submit">
            Send message
          </button>
        </form>
      </div>
    </div>
  );
}
