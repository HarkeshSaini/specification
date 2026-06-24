import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import DashboardIcon from './DashboardIcon';
import PageHeader from './PageHeader';
import UserForm from './UserForm';
import { unwrapData } from '../../services/adminApi';

export default function UserFormPage({ config }) {
  const { userId } = useParams();
  const navigate = useNavigate();
  const isEdit = Boolean(userId);
  const [initialData, setInitialData] = useState(null);
  const [loading, setLoading] = useState(isEdit);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!isEdit || !config.getByIdFn) {
      setInitialData(null);
      setLoading(false);
      return undefined;
    }
    let cancelled = false;
    setLoading(true);
    setError('');
    config
      .getByIdFn(userId)
      .then((res) => {
        if (!cancelled) setInitialData(unwrapData(res));
      })
      .catch((err) => {
        if (!cancelled) setError(err.message || 'Failed to load user');
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });
    return () => {
      cancelled = true;
    };
  }, [config, isEdit, userId]);

  async function handleSubmit(payload) {
    setSaving(true);
    setError('');
    try {
      if (isEdit) {
        await config.updateFn(userId, payload);
      } else {
        await config.createFn(payload);
      }
      navigate(config.listPath);
    } catch (err) {
      setError(err.message || 'Save failed');
      throw err;
    } finally {
      setSaving(false);
    }
  }

  const title = isEdit ? `Edit ${config.entityLabel}` : `Create ${config.entityLabel}`;
  const description = isEdit
    ? `Update account details for this ${config.entityLabel.toLowerCase()}.`
    : `Add a new ${config.entityLabel.toLowerCase()} to the platform.`;

  return (
    <div className="dashboard-page space-y-6">
      <PageHeader
        title={title}
        description={description}
        actions={
          <Link to={config.listPath} className="btn-gem-outline inline-flex items-center gap-2 rounded px-4 py-2 text-sm font-semibold">
            <DashboardIcon name="arrowLeft" className="h-4 w-4" />
            Back to list
          </Link>
        }
      />

      <div className="dashboard-panel p-5 sm:p-6 lg:p-8">
        {loading ? (
          <p className="py-12 text-center text-sm text-[#888888]">Loading user details…</p>
        ) : error && isEdit && !initialData ? (
          <div className="space-y-4 py-8 text-center">
            <div className="dashboard-alert dashboard-alert-error">{error}</div>
            <Link to={config.listPath} className="btn-gem-outline inline-flex rounded px-4 py-2 text-sm font-semibold">
              Back to list
            </Link>
          </div>
        ) : (
          <UserForm
            mode={isEdit ? 'edit' : 'create'}
            fields={config.formFields}
            initialData={initialData}
            loading={saving}
            error={error}
            onSubmit={handleSubmit}
            onCancel={() => navigate(config.listPath)}
          />
        )}
      </div>
    </div>
  );
}
