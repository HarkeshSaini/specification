import { useState } from 'react';

export default function ForgotPasswordForm({ onSwitchLogin }) {
  const [sent, setSent] = useState(false);

  function handleSubmit(e) {
    e.preventDefault();
    setSent(true);
  }

  if (sent) {
    return (
      <div className="animate-fade-up text-center">
        <p className="font-semibold text-white">Check your inbox</p>
        <p className="mt-2 text-sm text-slate-400">If an account exists for that email, we sent reset instructions.</p>
        <button type="button" onClick={onSwitchLogin} className="mt-5 text-sm font-medium text-indigo-400 hover:text-indigo-300">
          Back to sign in
        </button>
      </div>
    );
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <p className="text-sm text-slate-400">Enter your email and we will send you a reset link.</p>
      <div className="space-y-1.5">
        <label htmlFor="forgot-email" className="text-sm font-medium text-slate-300">Email address</label>
        <input id="forgot-email" type="email" required placeholder="you@company.com" className="w-full rounded-xl border border-white/10 bg-white/5 px-4 py-3 text-sm text-white outline-none focus:border-indigo-400/60" />
      </div>
      <button type="submit" className="btn-gradient w-full rounded-xl px-4 py-3.5 text-sm font-semibold text-white">
        Send reset link
      </button>
      <p className="text-center text-sm text-slate-500">
        Remember your password?{' '}
        <button type="button" onClick={onSwitchLogin} className="font-medium text-indigo-400 hover:text-indigo-300">
          Sign in
        </button>
      </p>
    </form>
  );
}
