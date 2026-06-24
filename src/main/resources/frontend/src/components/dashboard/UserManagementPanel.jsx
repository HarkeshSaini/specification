import { useCallback, useEffect, useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import DashboardIcon from './DashboardIcon';
import DeleteConfirmModal from './DeleteConfirmModal';
import EmptyState from './EmptyState';
import PageHeader from './PageHeader';
import Pagination from './Pagination';
import SearchFilter from './SearchFilter';
import StatusBadge from './StatusBadge';
import { unwrapPage } from '../../services/adminApi';
import { formatDate, formatRelativeDate, getInitials } from '../../utils/formatters';

export default function UserManagementPanel({
  title,
  description,
  entityLabel,
  listPath,
  listFn,
  deleteFn,
  formFields = [],
  extraColumns = [],
}) {
  const [items, setItems] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [search, setSearch] = useState('');
  const [selected, setSelected] = useState(null);
  const [deleteOpen, setDeleteOpen] = useState(false);
  const [actionLoading, setActionLoading] = useState(false);

  const createPath = `${listPath}/new`;
  const editPath = (id) => `${listPath}/${id}/edit`;

  const load = useCallback(async () => {
    setLoading(true);
    setError('');
    try {
      const res = await listFn(page, 10);
      const data = unwrapPage(res);
      setItems(data.items);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
    } catch (err) {
      setError(err.message || 'Failed to load data');
    } finally {
      setLoading(false);
    }
  }, [listFn, page]);

  useEffect(() => {
    load();
  }, [load]);

  const filtered = useMemo(() => {
    if (!search) return items;
    const q = search.toLowerCase();
    return items.filter(
      (item) =>
        item.fullName?.toLowerCase().includes(q) ||
        item.email?.toLowerCase().includes(q) ||
        item.username?.toLowerCase().includes(q) ||
        item.department?.toLowerCase().includes(q),
    );
  }, [items, search]);

  function openDelete(item) {
    setSelected(item);
    setDeleteOpen(true);
  }

  async function handleDelete() {
    if (!selected?.id) return;
    setActionLoading(true);
    try {
      await deleteFn(selected.id);
      setDeleteOpen(false);
      await load();
    } catch (err) {
      setError(err.message || 'Delete failed');
    } finally {
      setActionLoading(false);
    }
  }

  return (
    <div className="dashboard-page space-y-6">
      <PageHeader
        title={title}
        description={description}
        actions={
          <Link to={createPath} className="btn-gem-outline inline-flex items-center gap-2 rounded px-4 py-2 text-sm font-semibold">
            <DashboardIcon name="plus" className="h-4 w-4" />
            Add {entityLabel}
          </Link>
        }
      />

      <div className="dashboard-panel">
        <div className="dashboard-panel-toolbar">
          <p className="text-sm font-semibold text-[#1a1a1a]">List of {entityLabel.toLowerCase()}s</p>
          <div className="flex flex-wrap items-center gap-2">
            <SearchFilter placeholder={`Search ${entityLabel.toLowerCase()}s…`} onSearch={setSearch} />
            <Link to={createPath} className="btn-gem-outline inline-flex items-center gap-1.5 rounded px-3 py-1.5 text-xs font-semibold">
              <DashboardIcon name="plus" className="h-3.5 w-3.5" />
              Add New
            </Link>
          </div>
        </div>

        {error ? (
          <div className="dashboard-alert dashboard-alert-error mx-4 mt-4 sm:mx-5">{error}</div>
        ) : null}

        {loading ? (
          <div className="p-8 text-center text-sm text-[#888888]">Loading {entityLabel.toLowerCase()}s…</div>
        ) : filtered.length === 0 ? (
          <EmptyState
            icon="users"
            title={`No ${entityLabel.toLowerCase()}s found`}
            description={search ? 'Try a different search term.' : `Create your first ${entityLabel.toLowerCase()} to get started.`}
            action={
              !search ? (
                <Link to={createPath} className="btn-gem-primary inline-flex rounded px-4 py-2.5 text-sm font-semibold">
                  Add {entityLabel}
                </Link>
              ) : null
            }
          />
        ) : (
          <div className="dashboard-table-wrap">
            <table className="dashboard-table w-full min-w-[720px]">
              <thead>
                <tr>
                  <th>User</th>
                  <th>Contact</th>
                  {extraColumns.map((col) => (
                    <th key={col.key}>{col.label}</th>
                  ))}
                  <th>Status</th>
                  <th>Last login</th>
                  <th className="text-right">Actions</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map((item) => (
                  <tr key={item.id}>
                    <td>
                      <div className="flex items-center gap-3">
                        <div className="dashboard-avatar">{getInitials(item.fullName, item.email)}</div>
                        <div>
                          <p className="font-medium text-[#1a1a1a]">{item.fullName || item.email}</p>
                          <p className="text-xs text-[#888888]">@{item.username || item.email?.split('@')[0]}</p>
                        </div>
                      </div>
                    </td>
                    <td>
                      <p className="text-sm text-[#333333]">{item.email}</p>
                      <p className="text-xs text-[#888888]">{item.phoneNumber || '—'}</p>
                    </td>
                    {extraColumns.map((col) => (
                      <td key={col.key}>
                        <span className="text-sm text-[#333333]">{col.render ? col.render(item) : item[col.key] || '—'}</span>
                      </td>
                    ))}
                    <td>
                      <StatusBadge active={item.active !== false} />
                    </td>
                    <td>
                      <span className="text-sm text-[#888888]" title={formatDate(item.lastLoginAt)}>
                        {formatRelativeDate(item.lastLoginAt)}
                      </span>
                    </td>
                    <td>
                      <div className="flex justify-end gap-1">
                        <Link to={editPath(item.id)} className="gem-link text-sm font-medium" title="Edit">
                          Edit
                        </Link>
                        {deleteFn ? (
                          <button type="button" onClick={() => openDelete(item)} className="dashboard-icon-btn text-[#c62828] hover:text-[#b71c1c]" title="Delete">
                            <DashboardIcon name="trash" className="h-4 w-4" />
                          </button>
                        ) : null}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        <Pagination page={page} totalPages={totalPages} totalElements={totalElements} onPageChange={setPage} loading={loading} />
      </div>

      <DeleteConfirmModal
        open={deleteOpen}
        onClose={() => setDeleteOpen(false)}
        onConfirm={handleDelete}
        loading={actionLoading}
        title={`Delete ${entityLabel}`}
        message={`Deactivate ${selected?.fullName || selected?.email || 'this user'}? They will no longer be able to sign in.`}
      />
    </div>
  );
}
