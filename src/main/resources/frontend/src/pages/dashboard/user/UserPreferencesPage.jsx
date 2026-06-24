import PageHeader from '../../../components/dashboard/PageHeader';
import { DashboardField, DashboardInput, DashboardSelect } from '../../../components/dashboard/DashboardField';
import { useTheme } from '../../../context/ThemeContext';
import { useDashboardProfile } from '../../../hooks/useDashboardProfile';

const THEMES = [
  { id: 'dark', label: 'Dark', description: 'Deep space aesthetic' },
  { id: 'light', label: 'Light', description: 'Clean and bright' },
  { id: 'ocean', label: 'Ocean', description: 'Cool blue tones' },
];

const LANGUAGES = [
  { id: 'en', label: 'English' },
  { id: 'es', label: 'Spanish' },
  { id: 'fr', label: 'French' },
  { id: 'de', label: 'German' },
];

export default function UserPreferencesPage() {
  const { profile, error } = useDashboardProfile('user');
  const { theme, setTheme } = useTheme();

  return (
    <div className="dashboard-page space-y-6">
      <PageHeader title="Preferences" description="Customize your experience on the platform." />
      {error ? <div className="dashboard-alert dashboard-alert-error">{error}</div> : null}

      <div className="grid gap-6 lg:grid-cols-2">
        <section className="dashboard-panel p-6">
          <h3 className="dashboard-form-section-title">Appearance</h3>
          <p className="dashboard-field-hint mb-5">Choose your preferred color theme. Changes apply instantly.</p>
          <div className="grid gap-3">
            {THEMES.map((t) => (
              <button
                key={t.id}
                type="button"
                onClick={() => setTheme(t.id)}
                className={`flex items-center justify-between rounded-lg border px-4 py-3 text-left transition-all ${
                  theme === t.id
                    ? 'border-[#c05a2b] bg-[#fef3ed] ring-1 ring-[#c05a2b]/25'
                    : 'border-[#e0e0e0] bg-white hover:border-[#cccccc] hover:bg-[#fafafa]'
                }`}
              >
                <div>
                  <p className="font-medium text-[#1a1a1a]">{t.label}</p>
                  <p className="text-xs text-[#888888]">{t.description}</p>
                </div>
                {theme === t.id ? <span className="font-bold text-[#c05a2b]">✓</span> : null}
              </button>
            ))}
          </div>
        </section>

        <section className="dashboard-panel p-6">
          <h3 className="dashboard-form-section-title">Localization</h3>
          <p className="dashboard-field-hint mb-5">Language and timezone settings from your profile.</p>
          <div className="dashboard-form-grid dashboard-form-grid-1">
            <DashboardField label="Language" id="pref-lang" hint="Profile language sync coming in a future update.">
              <DashboardSelect id="pref-lang" defaultValue={profile?.language || 'en'} disabled>
                {LANGUAGES.map((lang) => (
                  <option key={lang.id} value={lang.id}>{lang.label}</option>
                ))}
              </DashboardSelect>
            </DashboardField>
            <DashboardField label="Timezone" id="pref-tz">
              <DashboardInput id="pref-tz" readOnly value={profile?.timezone || 'UTC'} />
            </DashboardField>
          </div>
        </section>
      </div>
    </div>
  );
}
