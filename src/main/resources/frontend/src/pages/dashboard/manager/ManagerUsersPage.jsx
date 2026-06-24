import { useCallback, useEffect, useMemo, useState } from 'react';
import DashboardIcon from '../../../components/dashboard/DashboardIcon';
import EmptyState from '../../../components/dashboard/EmptyState';
import PageHeader from '../../../components/dashboard/PageHeader';
import SearchFilter from '../../../components/dashboard/SearchFilter';
import StatusBadge from '../../../components/dashboard/StatusBadge';
import { useAuth } from '../../../context/AuthContext';
import { fetchManagerWebsiteUsers, listWebsiteUsers, unwrapPage } from '../../../services/adminApi';
import { formatRelativeDate, getInitials } from '../../../utils/formatters';

export default function ManagerUsersPage() {
  const { isAdmin } = useAuth();
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [search, setSearch] = useState('');

  const load = useCallback(async () => {
    setLoading(true);
    setError('');
    try {
      if (isAdmin) {
        const res = await listWebsiteUsers(0, 50);
        setItems(unwrapPage(res).items);
      } else {
        setItems(await fetchManagerWebsiteUsers());
      }
    } catch (err) {
      setError(err.message || 'Failed to load users');
    } finally {
      setLoading(false);
    }
  }, [isAdmin]);

  useEffect(() => {
    load();
  }, [load]);

  const filtered = useMemo(() => {
    if (!search) return items;
    const q = search.toLowerCase();
    return items.filter(
      (item) =>
        item.fullName?.toLowerCase().includes(q) ||
        item.email?.toLowerCase().includes(q),
    );
  }, [items, search]);

  return (
    <div className="dashboard-page space-y-6">
      <PageHeader title="Website Users" description="View and monitor website users in your management scope." />

      <div className="dashboard-panel">
        <div className="border-b border-white/5 p-4 sm:p-5">
          <SearchFilter placeholder="Search users…" onSearch={setSearch} />
        </div>

        {error ? <div className="mx-4 mt-4 rounded-xl border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-300 sm:mx-5">{error}</div> : null}

        {loading ? (
          <div className="p-8 text-center text-sm text-slate-500">Loading users…</div>
        ) : filtered.length === 0 ? (
          <EmptyState icon="globe" title="No users found" description="No website users match your search." />
        ) : (
          <div className="grid gap-4 p-4 sm:grid-cols-2 sm:p-5 lg:grid-cols-3">
            {filtered.map((user) => (
              <div key={user.id} className="dashboard-user-card">
                <div className="flex items-start justify-between gap-3">
                  <div className="flex items-center gap-3">
                    <div className="dashboard-avatar">{getInitials(user.fullName, user.email)}</div>
                    <div>
                      <p className="font-medium text-white">{user.fullName || user.email}</p>
                      <p className="text-xs text-slate-500">{user.email}</p>
                    </div>
                  </div>
                  <StatusBadge active={user.active !== false} />
                </div>
                <div className="mt-4 grid grid-cols-2 gap-2 text-xs">
                  <div className="rounded-lg bg-white/[0.03] px-3 py-2">
                    <p className="text-slate-500">Language</p>
                    <p className="mt-0.5 font-medium text-slate-300">{user.language || 'en'}</p>
                  </div>
                  <div className="rounded-lg bg-white/[0.03] px-3 py-2">
                    <p className="text-slate-500">Last login</p>
                    <p className="mt-0.5 font-medium text-slate-300">{formatRelativeDate(user.lastLoginAt)}</p>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
