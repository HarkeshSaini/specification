import { useEffect, useState } from 'react';
import { DashboardField, DashboardInput } from './DashboardField';
import { splitFullName } from '../../utils/formatters';

const EMPTY = {
  firstName: '',
  lastName: '',
  email: '',
  username: '',
  password: '',
  phoneNumber: '',
  department: '',
  designation: '',
  managedRegion: '',
  language: 'en',
  timezone: 'UTC',
  theme: 'dark',
};

export default function UserForm({
  onSubmit,
  onCancel,
  fields = [],
  initialData,
  loading,
  mode = 'create',
  error: externalError,
}) {
  const [form, setForm] = useState(EMPTY);
  const [error, setError] = useState('');

  useEffect(() => {
    setError('');
    if (initialData) {
      const { firstName, lastName } = splitFullName(initialData.fullName);
      setForm({
        ...EMPTY,
        firstName,
        lastName,
        email: initialData.email ?? '',
        username: initialData.username ?? '',
        phoneNumber: initialData.phoneNumber ?? '',
        department: initialData.department ?? '',
        designation: initialData.designation ?? '',
        managedRegion: initialData.managedRegion ?? '',
        language: initialData.language ?? 'en',
        timezone: initialData.timezone ?? 'UTC',
        theme: initialData.theme ?? 'dark',
      });
    } else {
      setForm(EMPTY);
    }
  }, [initialData]);

  function updateField(field, value) {
    setForm((prev) => ({ ...prev, [field]: value }));
  }

  function buildPayload() {
    const base = {
      email: form.email.trim(),
      username: form.username.trim() || form.email.split('@')[0],
      fullName: `${form.firstName} ${form.lastName}`.trim(),
      phoneNumber: form.phoneNumber || undefined,
      active: true,
    };
    if (mode === 'create' || form.password) base.password = form.password;

    const payload = { ...base };
    if (fields.includes('department')) payload.department = form.department || undefined;
    if (fields.includes('designation')) payload.designation = form.designation || undefined;
    if (fields.includes('managedRegion')) payload.managedRegion = form.managedRegion || undefined;
    if (fields.includes('language')) payload.language = form.language;
    if (fields.includes('timezone')) payload.timezone = form.timezone;
    if (fields.includes('theme')) payload.theme = form.theme;
    if (fields.includes('allAccess')) payload.allAccess = true;
    return payload;
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError('');
    try {
      await onSubmit(buildPayload());
    } catch (err) {
      setError(err.message || 'Save failed');
    }
  }

  const displayError = externalError || error;
  const showPassword = mode === 'create';

  return (
    <form onSubmit={handleSubmit} className="dashboard-form">
      <section className="dashboard-form-section">
        <h3 className="dashboard-form-section-title">Personal details</h3>
        <div className="dashboard-form-grid dashboard-form-grid-2">
          <Field label="First name" id="uf-first" required value={form.firstName} onChange={(v) => updateField('firstName', v)} />
          <Field label="Last name" id="uf-last" required value={form.lastName} onChange={(v) => updateField('lastName', v)} />
          <Field label="Email address" id="uf-email" type="email" required value={form.email} onChange={(v) => updateField('email', v)} placeholder="name@company.com" />
          <Field label="Phone number" id="uf-phone" type="tel" value={form.phoneNumber} onChange={(v) => updateField('phoneNumber', v)} placeholder="+1 234 567 8900" />
          <div className="dashboard-form-span-2">
            <Field label="Username" id="uf-user" value={form.username} onChange={(v) => updateField('username', v)} placeholder="Defaults to email prefix if empty" />
          </div>
        </div>
      </section>

      <section className="dashboard-form-section">
        <h3 className="dashboard-form-section-title">Account credentials</h3>
        <div className="dashboard-form-grid dashboard-form-grid-1">
          {showPassword ? (
            <Field label="Password" id="uf-pass" type="password" required minLength={8} value={form.password} onChange={(v) => updateField('password', v)} hint="Minimum 8 characters" autoComplete="new-password" />
          ) : (
            <Field label="New password" id="uf-pass" type="password" minLength={8} value={form.password} onChange={(v) => updateField('password', v)} hint="Leave blank to keep the current password" autoComplete="new-password" />
          )}
        </div>
      </section>

      {(fields.includes('department') || fields.includes('designation') || fields.includes('managedRegion')) ? (
        <section className="dashboard-form-section">
          <h3 className="dashboard-form-section-title">Role details</h3>
          <div className="dashboard-form-grid dashboard-form-grid-2">
            {fields.includes('department') ? (
              <Field label="Department" id="uf-dept" value={form.department} onChange={(v) => updateField('department', v)} placeholder="e.g. Platform Engineering" />
            ) : null}
            {fields.includes('designation') ? (
              <Field label="Designation" id="uf-desig" value={form.designation} onChange={(v) => updateField('designation', v)} placeholder="e.g. Senior Administrator" />
            ) : null}
            {fields.includes('managedRegion') ? (
              <Field label="Managed region" id="uf-region" value={form.managedRegion} onChange={(v) => updateField('managedRegion', v)} placeholder="e.g. North India" />
            ) : null}
          </div>
        </section>
      ) : null}

      {fields.includes('language') ? (
        <section className="dashboard-form-section">
          <h3 className="dashboard-form-section-title">Preferences</h3>
          <div className="dashboard-form-grid dashboard-form-grid-3">
            <Field label="Language" id="uf-lang" value={form.language} onChange={(v) => updateField('language', v)} />
            <Field label="Timezone" id="uf-tz" value={form.timezone} onChange={(v) => updateField('timezone', v)} />
            <Field label="Theme" id="uf-theme" value={form.theme} onChange={(v) => updateField('theme', v)} />
          </div>
        </section>
      ) : null}

      {displayError ? (
        <div className="dashboard-alert dashboard-alert-error">{displayError}</div>
      ) : null}

      <div className="dashboard-form-actions">
        <button type="button" onClick={onCancel} className="btn-gem-outline rounded px-5 py-2.5 text-sm font-semibold">
          Cancel
        </button>
        <button type="submit" disabled={loading} className="btn-gem-primary rounded px-5 py-2.5 text-sm font-semibold disabled:opacity-60">
          {loading ? 'Saving…' : mode === 'create' ? 'Create user' : 'Save changes'}
        </button>
      </div>
    </form>
  );
}

function Field({ label, id, value, onChange, type = 'text', required, minLength, placeholder, hint, autoComplete }) {
  return (
    <DashboardField label={label} id={id} required={required} hint={hint}>
      <DashboardInput
        id={id}
        type={type}
        required={required}
        minLength={minLength}
        value={value}
        placeholder={placeholder}
        autoComplete={autoComplete}
        onChange={(e) => onChange(e.target.value)}
      />
    </DashboardField>
  );
}
