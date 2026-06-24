import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { fetchAdminProfile, fetchManagerProfile, fetchSuperAdminProfile, fetchWebsiteProfile, unwrapData } from '../services/adminApi';

export function useDashboardProfile(scope = 'auto') {
  const { role, isAdmin, isSuperAdmin, isManager, isWebsiteUser } = useAuth();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    let cancelled = false;

    async function load() {
      setLoading(true);
      setError('');
      try {
        let res;
        if (scope === 'super-admin' || (scope === 'auto' && isSuperAdmin)) {
          res = await fetchSuperAdminProfile();
        } else if (scope === 'admin' || (scope === 'auto' && isAdmin)) {
          res = await fetchAdminProfile();
        } else if (scope === 'manager' || (scope === 'auto' && isManager)) {
          res = await fetchManagerProfile();
        } else if (scope === 'user' || (scope === 'auto' && isWebsiteUser)) {
          res = await fetchWebsiteProfile();
        } else if (isSuperAdmin) {
          res = await fetchSuperAdminProfile();
        } else if (isAdmin) {
          res = await fetchAdminProfile();
        } else {
          throw new Error('Unable to resolve profile for current role');
        }
        if (!cancelled) setProfile(unwrapData(res));
      } catch (err) {
        if (!cancelled) setError(err.message || 'Failed to load profile');
      } finally {
        if (!cancelled) setLoading(false);
      }
    }

    load();
    return () => {
      cancelled = true;
    };
  }, [scope, role, isAdmin, isSuperAdmin, isManager, isWebsiteUser]);

  return { profile, loading, error, role };
}
