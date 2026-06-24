import { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { createAdmin, createManager, createWebsiteUser, unwrapData } from '../../services/api';
import { registerWebsiteUser } from '../../services/authApi';
import { ROLES } from '../../utils/roles';

const ACCOUNT_TYPES = [
  { id: ROLES.WEBSITE_USER, label: 'Website User', public: true },
  { id: ROLES.ADMIN, label: 'Admin', public: false },
  { id: ROLES.MANAGER, label: 'Manager', public: false },
];

function buildAdminPayload(form) {
  return {
    email: form.email.trim(),
    username: form.username.trim() || form.email.split('@')[0],
    password: form.password,
    fullName: `${form.firstName} ${form.lastName}`.trim(),
    phoneNumber: form.phoneNumber || undefined,
    department: form.department || undefined,
    designation: form.designation || undefined,
    active: true,
  };
}

function buildWebsitePayload(form) {
  return {
    email: form.email.trim(),
    username: form.username.trim() || form.email.split('@')[0],
    password: form.password,
    fullName: `${form.firstName} ${form.lastName}`.trim(),
    active: true,
  };
}

export default function CreateAccountForm({ onSwitchLogin, onSuccess }) {
  const { isAdmin, isAuthenticated } = useAuth();
  const [accountType, setAccountType] = useState(ROLES.WEBSITE_USER);
  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    email: '',
    username: '',
    password: '',
    phoneNumber: '',
    department: '',
    designation: '',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  function updateField(field, value) {
    setForm((prev) => ({ ...prev, [field]: value }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const selected = ACCOUNT_TYPES.find((t) => t.id === accountType);

      if (accountType === ROLES.WEBSITE_USER) {
        const payload = buildWebsitePayload(form);
        await registerWebsiteUser(payload);
      } else if (accountType === ROLES.ADMIN) {
        if (!isAuthenticated || !isAdmin) {
          throw new Error('Creating an Admin requires Admin login. Sign in as Admin first.');
        }
        const payload = buildAdminPayload(form);
        const response = await createAdmin(payload);
        unwrapData(response);
      } else if (accountType === ROLES.MANAGER) {
        if (!isAuthenticated || !isAdmin) {
          throw new Error('Creating a Manager requires Admin login.');
        }
        const payload = buildAdminPayload(form);
        const response = await createManager(payload);
        unwrapData(response);
      } else if (selected?.public === false) {
        throw new Error('This account type requires Admin privileges.');
      }

      setSuccess(true);
      onSuccess?.();
    } catch (err) {
      setError(err.message || 'Account creation failed');
    } finally {
      setLoading(false);
    }
  }

  if (success) {
    return (
      <div className="animate-fade-up text-center">
        <div className="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-full bg-emerald-500/15 text-emerald-400 ring-1 ring-emerald-500/30">
          <svg className="h-7 w-7" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
            <path strokeLinecap="round" strokeLinejoin="round" d="M9 12.75L11.25 15 15 9.75M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        </div>
        <p className="font-semibold text-white">Account created</p>
        <p className="mt-2 text-sm text-slate-400">You can now sign in with your credentials.</p>
        <button type="button" onClick={onSwitchLogin} className="mt-5 text-sm font-medium text-indigo-400 hover:text-indigo-300">
          Go to sign in
        </button>
      </div>
    );
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div className="space-y-1.5">
        <label htmlFor="reg-type" className="text-sm font-medium text-slate-300">Account type</label>
        <select
          id="reg-type"
          value={accountType}
          onChange={(e) => setAccountType(e.target.value)}
          className="theme-input-surface w-full rounded-xl border px-4 py-3 text-sm outline-none"
        >
          {ACCOUNT_TYPES.map((type) => (
            <option key={type.id} value={type.id}>
              {type.label}
            </option>
          ))}
        </select>
        {accountType !== ROLES.WEBSITE_USER ? (
          <p className="text-xs text-amber-400/90">Admin/Manager creation requires an Admin login.</p>
        ) : null}
      </div>

      <div className="grid gap-4 sm:grid-cols-2">
        <div className="space-y-1.5">
          <label htmlFor="reg-first" className="text-sm font-medium text-slate-300">First name</label>
          <input
            id="reg-first"
            required
            value={form.firstName}
            onChange={(e) => updateField('firstName', e.target.value)}
            className="theme-input-surface w-full rounded-xl border px-4 py-3 text-sm outline-none"
          />
        </div>
        <div className="space-y-1.5">
          <label htmlFor="reg-last" className="text-sm font-medium text-slate-300">Last name</label>
          <input
            id="reg-last"
            required
            value={form.lastName}
            onChange={(e) => updateField('lastName', e.target.value)}
            className="theme-input-surface w-full rounded-xl border px-4 py-3 text-sm outline-none"
          />
        </div>
      </div>

      <div className="space-y-1.5">
        <label htmlFor="reg-email" className="text-sm font-medium text-slate-300">Email</label>
        <input
          id="reg-email"
          type="email"
          required
          value={form.email}
          onChange={(e) => updateField('email', e.target.value)}
          placeholder="you@company.com"
          className="theme-input-surface w-full rounded-xl border px-4 py-3 text-sm outline-none"
        />
      </div>

      <div className="space-y-1.5">
        <label htmlFor="reg-username" className="text-sm font-medium text-slate-300">Username (optional)</label>
        <input
          id="reg-username"
          value={form.username}
          onChange={(e) => updateField('username', e.target.value)}
          placeholder="defaults to email prefix"
          className="theme-input-surface w-full rounded-xl border px-4 py-3 text-sm outline-none"
        />
      </div>

      <div className="space-y-1.5">
        <label htmlFor="reg-pass" className="text-sm font-medium text-slate-300">Password</label>
        <input
          id="reg-pass"
          type="password"
          required
          minLength={8}
          value={form.password}
          onChange={(e) => updateField('password', e.target.value)}
          className="theme-input-surface w-full rounded-xl border px-4 py-3 text-sm outline-none"
        />
      </div>

      {accountType === ROLES.ADMIN || accountType === ROLES.MANAGER ? (
        <>
          <div className="space-y-1.5">
            <label htmlFor="reg-phone" className="text-sm font-medium text-slate-300">Phone (optional)</label>
            <input
              id="reg-phone"
              value={form.phoneNumber}
              onChange={(e) => updateField('phoneNumber', e.target.value)}
              className="theme-input-surface w-full rounded-xl border px-4 py-3 text-sm outline-none"
            />
          </div>
          <div className="grid gap-4 sm:grid-cols-2">
            <div className="space-y-1.5">
              <label htmlFor="reg-dept" className="text-sm font-medium text-slate-300">Department</label>
              <input
                id="reg-dept"
                value={form.department}
                onChange={(e) => updateField('department', e.target.value)}
                className="theme-input-surface w-full rounded-xl border px-4 py-3 text-sm outline-none"
              />
            </div>
            <div className="space-y-1.5">
              <label htmlFor="reg-desig" className="text-sm font-medium text-slate-300">Designation</label>
              <input
                id="reg-desig"
                value={form.designation}
                onChange={(e) => updateField('designation', e.target.value)}
                className="theme-input-surface w-full rounded-xl border px-4 py-3 text-sm outline-none"
              />
            </div>
          </div>
        </>
      ) : null}

      {error ? (
        <div role="alert" className="rounded-xl border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-300">
          {error}
        </div>
      ) : null}

      <button
        type="submit"
        disabled={loading}
        className="btn-gradient w-full rounded-xl px-4 py-3.5 text-sm font-semibold text-white disabled:opacity-60"
      >
        {loading ? 'Creating account…' : 'Create account'}
      </button>

      <p className="text-center text-sm text-slate-500">
        Already have an account?{' '}
        <button type="button" onClick={onSwitchLogin} className="font-medium text-indigo-400 hover:text-indigo-300">
          Sign in
        </button>
      </p>
    </form>
  );
}
