import { Link } from 'react-router-dom';

export default function BrandLogo({ compact = false, compactOnMobile = false, variant = 'dark' }) {
  const textPrimary = variant === 'light' ? 'text-[#1a1a1a]' : 'text-white';
  const textAccent = variant === 'light' ? 'text-[#c05a2b]' : 'text-indigo-400/80';
  const hideText = compact;
  const hideTextMobile = compactOnMobile && !compact;

  return (
    <Link to="/" className={`group flex min-w-0 items-center gap-2 sm:gap-3 ${hideTextMobile ? 'brand-logo-compact-mobile' : ''}`}>
      <div className="relative flex h-9 w-9 shrink-0 items-center justify-center rounded-lg bg-[#c05a2b] shadow-sm transition-transform group-hover:scale-105 sm:h-10 sm:w-10">
        <svg className="h-5 w-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2} aria-hidden="true">
          <path strokeLinecap="round" strokeLinejoin="round" d="M9.813 15.904L9 18.75l-.813-2.846a4.5 4.5 0 00-3.09-3.09L2.25 12l2.846-.813a4.5 4.5 0 003.09-3.09L9 5.25l.813 2.846a4.5 4.5 0 003.09 3.09L15.75 12l-2.846.813a4.5 4.5 0 00-3.09 3.09z" />
        </svg>
      </div>
      {!hideText ? (
        <div className={hideTextMobile ? 'brand-logo-text' : ''}>
          <p className={`text-sm font-bold tracking-tight sm:text-base ${textPrimary}`}>Specification</p>
          <p className={`text-[10px] font-semibold uppercase tracking-widest ${textAccent}`}>Platform</p>
        </div>
      ) : null}
    </Link>
  );
}
