import { useCallback, useEffect, useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import DashboardIcon from '../../../components/dashboard/DashboardIcon';
import DeleteConfirmModal from '../../../components/dashboard/DeleteConfirmModal';
import EmptyState from '../../../components/dashboard/EmptyState';
import PageHeader from '../../../components/dashboard/PageHeader';
import Pagination from '../../../components/dashboard/Pagination';
import SearchFilter from '../../../components/dashboard/SearchFilter';
import { useAuth } from '../../../context/AuthContext';
import { deleteBlog, listBlogs, unwrapPage } from '../../../services/adminApi';
import { formatDate, formatRelativeDate } from '../../../utils/formatters';

function statusClass(status) {
  switch (status) {
    case 'PUBLISHED':
      return 'dashboard-status-badge dashboard-status-badge--active';
    case 'ARCHIVED':
      return 'dashboard-status-badge dashboard-status-badge--inactive';
    default:
      return 'dashboard-status-badge dashboard-status-badge--pending';
  }
}

export default function AdminBlogPage() {
  const { isSuperAdmin } = useAuth();
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

  const load = useCallback(async () => {
    setLoading(true);
    setError('');
    try {
      const res = await listBlogs(page, 10);
      const data = unwrapPage(res);
      setItems(data.items);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
    } catch (err) {
      setError(err.message || 'Failed to load blogs');
    } finally {
      setLoading(false);
    }
  }, [page]);

  useEffect(() => {
    load();
  }, [load]);

  const filtered = useMemo(() => {
    if (!search) return items;
    const q = search.toLowerCase();
    return items.filter(
      (item) =>
        item.title?.toLowerCase().includes(q) ||
        item.slug?.toLowerCase().includes(q) ||
        item.category?.toLowerCase().includes(q),
    );
  }, [items, search]);

  async function handleDelete() {
    if (!selected?.id) return;
    setActionLoading(true);
    try {
      await deleteBlog(selected.id);
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
        title="Blog posts"
        description="Create and update articles. Only Super Admins can delete posts."
        actions={
          <Link to="/admin/blog/new" className="btn-gem-outline inline-flex items-center gap-2 rounded px-4 py-2 text-sm font-semibold">
            <DashboardIcon name="plus" className="h-4 w-4" />
            New post
          </Link>
        }
      />

      <div className="dashboard-panel">
        <div className="dashboard-panel-toolbar">
          <p className="text-sm font-semibold text-[#1a1a1a]">All blog posts</p>
          <SearchFilter placeholder="Search by title, slug, category…" onSearch={setSearch} />
        </div>

        {error ? <div className="dashboard-alert dashboard-alert-error mx-4 mt-4 sm:mx-5">{error}</div> : null}

        {loading ? (
          <div className="p-8 text-center text-sm text-[#888888]">Loading blog posts…</div>
        ) : filtered.length === 0 ? (
          <EmptyState
            icon="globe"
            title="No blog posts yet"
            description="Create your first article to show on the public blog page."
            action={
              <Link to="/admin/blog/new" className="btn-gem-primary inline-flex rounded px-4 py-2.5 text-sm font-semibold">
                New post
              </Link>
            }
          />
        ) : (
          <div className="dashboard-table-wrap">
            <table className="dashboard-table w-full min-w-[720px]">
              <thead>
                <tr>
                  <th>Title</th>
                  <th>Slug</th>
                  <th>Category</th>
                  <th>Status</th>
                  <th>Published</th>
                  <th>Views</th>
                  <th className="text-right">Actions</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map((item) => (
                  <tr key={item.id}>
                    <td>
                      <p className="font-medium text-[#1a1a1a]">{item.title}</p>
                      <p className="text-xs text-[#888888] line-clamp-1">{item.summary || '—'}</p>
                    </td>
                    <td>
                      <code className="text-xs text-[#555555]">{item.slug}</code>
                    </td>
                    <td>{item.category || '—'}</td>
                    <td>
                      <span className={statusClass(item.status)}>{item.status || 'DRAFT'}</span>
                    </td>
                    <td>
                      <span className="text-sm text-[#888888]" title={formatDate(item.publishedAt)}>
                        {item.publishedAt ? formatRelativeDate(item.publishedAt) : '—'}
                      </span>
                    </td>
                    <td>{item.views ?? 0}</td>
                    <td>
                      <div className="flex justify-end gap-2">
                        {item.status === 'PUBLISHED' && item.slug ? (
                          <a href={`/blog/${item.slug}`} target="_blank" rel="noreferrer" className="gem-link text-sm">
                            View
                          </a>
                        ) : null}
                        <Link to={`/admin/blog/${item.id}/edit`} className="gem-link text-sm font-medium">
                          Edit
                        </Link>
                        {isSuperAdmin ? (
                          <button
                            type="button"
                            onClick={() => { setSelected(item); setDeleteOpen(true); }}
                            className="dashboard-icon-btn text-[#c62828]"
                            title="Delete"
                          >
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
        title="Delete blog post"
        message={`Delete "${selected?.title || 'this post'}"? This cannot be undone.`}
      />
    </div>
  );
}
