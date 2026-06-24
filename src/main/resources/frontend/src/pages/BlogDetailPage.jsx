import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { getBlogBySlug } from '../services/blogApi';
import { formatDate } from '../utils/formatters';

export default function BlogDetailPage() {
  const { slug } = useParams();
  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    setError('');
    getBlogBySlug(slug)
      .then((data) => {
        if (!cancelled) setPost(data);
      })
      .catch((err) => {
        if (!cancelled) setError(err.message || 'Article not found');
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });
    return () => {
      cancelled = true;
    };
  }, [slug]);

  if (loading) {
    return (
      <div className="site-page">
        <div className="site-page-inner site-page-narrow">
          <p className="site-blog-loading">Loading article…</p>
        </div>
      </div>
    );
  }

  if (error || !post) {
    return (
      <div className="site-page">
        <div className="site-page-inner site-page-narrow site-blog-detail-empty">
          <h1 className="site-page-title">Article not found</h1>
          <p className="site-page-lead">{error || 'This post may have been removed or is not published yet.'}</p>
          <Link to="/blog" className="site-blog-link">← Back to blog</Link>
        </div>
      </div>
    );
  }

  return (
    <article className="site-page site-blog-detail">
      <div className="site-page-inner site-blog-detail-inner">
        <Link to="/blog" className="site-blog-back">← All articles</Link>

        <header className="site-blog-detail-header">
          <div className="site-blog-detail-meta">
            {post.category ? <span className="site-blog-category">{post.category}</span> : null}
            <time dateTime={post.publishedAt}>{formatDate(post.publishedAt)}</time>
            {post.authorName ? <span>By {post.authorName}</span> : null}
            {post.views != null ? <span>{post.views} views</span> : null}
          </div>
          <h1 className="site-blog-detail-title">{post.title}</h1>
          {post.summary ? <p className="site-blog-detail-summary">{post.summary}</p> : null}
        </header>

        {post.featuredImageUrl ? (
          <div className="site-blog-detail-hero">
            <img src={post.featuredImageUrl} alt="" />
          </div>
        ) : null}

        <div className="site-blog-detail-content">
          {(post.content || '').split('\n').map((paragraph, index) =>
            paragraph.trim() ? <p key={index}>{paragraph}</p> : <br key={index} />,
          )}
        </div>

        {post.tags?.length ? (
          <footer className="site-blog-detail-tags">
            {post.tags.map((tag) => (
              <span key={tag} className="site-blog-tag">{tag}</span>
            ))}
          </footer>
        ) : null}
      </div>
    </article>
  );
}
