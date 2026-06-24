import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { DashboardField, DashboardInput, DashboardSelect, DashboardTextarea } from '../../../components/dashboard/DashboardField';
import PageHeader from '../../../components/dashboard/PageHeader';
import { createBlog, getBlogById, unwrapData, updateBlog } from '../../../services/adminApi';

const STATUS_OPTIONS = [
  { value: 'DRAFT', label: 'Draft' },
  { value: 'PUBLISHED', label: 'Published' },
  { value: 'ARCHIVED', label: 'Archived' },
];

const EMPTY = {
  title: '',
  slug: '',
  summary: '',
  content: '',
  category: '',
  tags: '',
  featuredImageUrl: '',
  status: 'DRAFT',
  metaTitle: '',
  metaDescription: '',
  metaKeywords: '',
};

export default function BlogFormPage() {
  const { blogId } = useParams();
  const navigate = useNavigate();
  const isEdit = Boolean(blogId);
  const [form, setForm] = useState(EMPTY);
  const [loading, setLoading] = useState(isEdit);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!isEdit) return undefined;
    let cancelled = false;
    setLoading(true);
    getBlogById(blogId)
      .then((res) => {
        if (cancelled) return;
        const data = unwrapData(res);
        setForm({
          title: data.title ?? '',
          slug: data.slug ?? '',
          summary: data.summary ?? '',
          content: data.content ?? '',
          category: data.category ?? '',
          tags: Array.isArray(data.tags) ? data.tags.join(', ') : '',
          featuredImageUrl: data.featuredImageUrl ?? '',
          status: data.status ?? 'DRAFT',
          metaTitle: data.metaTitle ?? '',
          metaDescription: data.metaDescription ?? '',
          metaKeywords: data.metaKeywords ?? '',
        });
      })
      .catch((err) => {
        if (!cancelled) setError(err.message || 'Failed to load blog');
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });
    return () => {
      cancelled = true;
    };
  }, [blogId, isEdit]);

  function updateField(field, value) {
    setForm((prev) => ({ ...prev, [field]: value }));
  }

  function buildPayload() {
    const tags = form.tags
      .split(',')
      .map((t) => t.trim())
      .filter(Boolean);
    return {
      title: form.title.trim(),
      slug: form.slug.trim() || undefined,
      summary: form.summary.trim() || undefined,
      content: form.content.trim() || undefined,
      category: form.category.trim() || undefined,
      tags: tags.length ? tags : undefined,
      featuredImageUrl: form.featuredImageUrl.trim() || undefined,
      status: form.status,
      metaTitle: form.metaTitle.trim() || undefined,
      metaDescription: form.metaDescription.trim() || undefined,
      metaKeywords: form.metaKeywords.trim() || undefined,
    };
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setSaving(true);
    setError('');
    try {
      const payload = buildPayload();
      if (isEdit) {
        await updateBlog(blogId, payload);
      } else {
        await createBlog(payload);
      }
      navigate('/admin/blog');
    } catch (err) {
      setError(err.message || 'Save failed');
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="dashboard-page space-y-6">
      <PageHeader
        title={isEdit ? 'Edit blog post' : 'Create blog post'}
        description={isEdit ? 'Update content, SEO, and publish status.' : 'Write a new article for the public blog.'}
        actions={
          <Link to="/admin/blog" className="btn-gem-outline inline-flex items-center gap-2 rounded px-4 py-2 text-sm font-semibold">
            Back to blog list
          </Link>
        }
      />

      <div className="dashboard-panel p-5 sm:p-6 lg:p-8">
        {loading ? (
          <p className="py-12 text-center text-sm text-[#888888]">Loading blog post…</p>
        ) : (
          <form onSubmit={handleSubmit} className="dashboard-form">
            <section className="dashboard-form-section">
              <h3 className="dashboard-form-section-title">Content</h3>
              <div className="dashboard-form-grid dashboard-form-grid-1">
                <DashboardField label="Title" id="blog-title" required>
                  <DashboardInput id="blog-title" required value={form.title} onChange={(e) => updateField('title', e.target.value)} />
                </DashboardField>
                <DashboardField label="URL slug" id="blog-slug" hint="Leave empty to auto-generate from title. Used in /blog/your-slug">
                  <DashboardInput id="blog-slug" value={form.slug} onChange={(e) => updateField('slug', e.target.value)} placeholder="why-specifications-matter" />
                </DashboardField>
                <DashboardField label="Summary" id="blog-summary">
                  <DashboardTextarea id="blog-summary" rows={3} value={form.summary} onChange={(e) => updateField('summary', e.target.value)} />
                </DashboardField>
                <DashboardField label="Content" id="blog-content">
                  <DashboardTextarea id="blog-content" rows={12} value={form.content} onChange={(e) => updateField('content', e.target.value)} />
                </DashboardField>
              </div>
            </section>

            <section className="dashboard-form-section">
              <h3 className="dashboard-form-section-title">Classification</h3>
              <div className="dashboard-form-grid dashboard-form-grid-2">
                <DashboardField label="Category" id="blog-category">
                  <DashboardInput id="blog-category" value={form.category} onChange={(e) => updateField('category', e.target.value)} />
                </DashboardField>
                <DashboardField label="Status" id="blog-status">
                  <DashboardSelect id="blog-status" value={form.status} onChange={(e) => updateField('status', e.target.value)}>
                    {STATUS_OPTIONS.map((opt) => (
                      <option key={opt.value} value={opt.value}>{opt.label}</option>
                    ))}
                  </DashboardSelect>
                </DashboardField>
                <DashboardField label="Tags" id="blog-tags" hint="Comma-separated">
                  <DashboardInput id="blog-tags" value={form.tags} onChange={(e) => updateField('tags', e.target.value)} placeholder="specs, api, ai" />
                </DashboardField>
                <DashboardField label="Featured image URL" id="blog-image">
                  <DashboardInput id="blog-image" value={form.featuredImageUrl} onChange={(e) => updateField('featuredImageUrl', e.target.value)} />
                </DashboardField>
              </div>
            </section>

            <section className="dashboard-form-section">
              <h3 className="dashboard-form-section-title">SEO</h3>
              <div className="dashboard-form-grid dashboard-form-grid-1">
                <DashboardField label="Meta title" id="blog-meta-title">
                  <DashboardInput id="blog-meta-title" value={form.metaTitle} onChange={(e) => updateField('metaTitle', e.target.value)} />
                </DashboardField>
                <DashboardField label="Meta description" id="blog-meta-desc">
                  <DashboardTextarea id="blog-meta-desc" rows={2} value={form.metaDescription} onChange={(e) => updateField('metaDescription', e.target.value)} />
                </DashboardField>
                <DashboardField label="Meta keywords" id="blog-meta-kw">
                  <DashboardInput id="blog-meta-kw" value={form.metaKeywords} onChange={(e) => updateField('metaKeywords', e.target.value)} />
                </DashboardField>
              </div>
            </section>

            {error ? <div className="dashboard-alert dashboard-alert-error">{error}</div> : null}

            <div className="dashboard-form-actions">
              <button type="button" onClick={() => navigate('/admin/blog')} className="btn-gem-outline rounded px-5 py-2.5 text-sm font-semibold">
                Cancel
              </button>
              <button type="submit" disabled={saving} className="btn-gem-primary rounded px-5 py-2.5 text-sm font-semibold disabled:opacity-60">
                {saving ? 'Saving…' : isEdit ? 'Save changes' : 'Publish / save'}
              </button>
            </div>
          </form>
        )}
      </div>
    </div>
  );
}
