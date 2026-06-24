import { useEffect } from 'react';

export default function Modal({ open, onClose, title, children, size = 'md' }) {
  useEffect(() => {
    if (!open) return undefined;
    const prev = document.body.style.overflow;
    document.body.style.overflow = 'hidden';
    const onKey = (e) => {
      if (e.key === 'Escape') onClose();
    };
    window.addEventListener('keydown', onKey);
    return () => {
      document.body.style.overflow = prev;
      window.removeEventListener('keydown', onKey);
    };
  }, [open, onClose]);

  if (!open) return null;

  const sizeClass = size === 'lg' ? 'sm:max-w-xl' : 'sm:max-w-md';

  return (
    <div className="fixed inset-0 z-[100] flex items-end justify-center sm:items-center sm:p-4 md:p-6" role="dialog" aria-modal="true" aria-label={title}>
      <button
        type="button"
        className="absolute inset-0 backdrop-blur-sm"
        style={{ backgroundColor: 'color-mix(in srgb, var(--bg-base) 82%, transparent)' }}
        onClick={onClose}
        aria-label="Close dialog"
      />
      <div className={`animate-fade-up relative w-full ${sizeClass} max-h-[92dvh] overflow-y-auto rounded-t-lg border border-[#e0e0e0] bg-white p-5 shadow-lg sm:max-h-[90dvh] sm:rounded-lg sm:p-6 md:p-8`}>
        <div className="mb-6 flex items-start justify-between gap-4">
          <h2 className="text-xl font-bold text-[#1a1a1a]">{title}</h2>
          <button
            type="button"
            onClick={onClose}
            className="rounded-lg p-1.5 text-[#888888] transition-colors hover:bg-[#f4f4f4] hover:text-[#1a1a1a]"
            aria-label="Close"
          >
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        {children}
      </div>
    </div>
  );
}
