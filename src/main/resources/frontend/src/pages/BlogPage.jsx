import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Pagination from '../components/dashboard/Pagination';
import { listPublishedBlogs } from '../services/blogApi';
import { formatDate } from '../utils/formatters';

export default function BlogPage() {
  const [page, setPage] = useState(0);
  const [posts, setPosts] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    setError('');
    listPublishedBlogs(page, 12)
      .then((data) => {
        if (cancelled) return;
        setPosts(data.items);
        setTotalPages(data.totalPages);
        setTotalElements(data.totalElements);
      })
      .catch((err) => {
        if (!cancelled) setError(err.message || 'Failed to load blog posts');
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });
    return () => {
      cancelled = true;
    };
  }, [page]);

  return (
    <div className="site-page">
      <div className="site-page-inner site-page-narrow">
        <p className="site-section-eyebrow">Blog</p>
        <h1 className="site-page-title">Insights & updates</h1>
        <p className="site-page-lead">News, guides, and product announcements from the Specification team.</p>

        {error ? <div className="site-blog-alert">{error}</div> : null}

        {loading ? (
          <div className="site-blog-loading">Loading articles…</div>
        ) : posts.length === 0 ? (
          <div className="site-blog-empty">
            <p>No published articles yet. Check back soon.</p>
          </div>
        ) : (
          <>
            <div className="site-blog-grid">
              {posts.map((post) => (
                <article key={post.id} className="site-blog-card site-blog-card--rich">
                  {post.featuredImageUrl ? (
                    <Link to={`/blog/${post.slug}`} className="site-blog-card-image-wrap">
                      <img src={post.featuredImageUrl} alt="" className="site-blog-card-image" loading="lazy" />
                    </Link>
                  ) : (
                    <Link to={`/blog/${post.slug}`} className="site-blog-card-image-placeholder" aria-hidden="true">
                      <span>S</span>
                    </Link>
                  )}
                  <div className="site-blog-card-body">
                    <div className="site-blog-card-meta">
                      {post.category ? <span className="site-blog-category">{post.category}</span> : null}
                      <time className="site-blog-date" dateTime={post.publishedAt}>
                        {formatDate(post.publishedAt)}
                      </time>
                    </div>
                    <h2 className="site-blog-title">
                      <Link to={`/blog/${post.slug}`}>{post.title}</Link>
                    </h2>
                    <p className="site-blog-excerpt">{post.summary || 'Read the full article for more.'}</p>
                    <Link to={`/blog/${post.slug}`} className="site-blog-link">
                      Read more →
                    </Link>
                  </div>
                </article>
              ))}
            </div>
            {totalPages > 1 ? (
              <div className="site-blog-pagination">
                <Pagination page={page} totalPages={totalPages} totalElements={totalElements} onPageChange={setPage} loading={loading} />
              </div>
            ) : null}
          </>
        )}
      </div>
    </div>
  );
}
