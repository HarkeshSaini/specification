const API_BASE = import.meta.env.VITE_API_BASE ?? '';

async function getJson(path) {
  const res = await fetch(`${API_BASE}${path}`);
  let body = null;
  try {
    body = await res.json();
  } catch {
    /* ignore */
  }
  if (!res.ok) {
    const message = body?.message ?? body?.data?.message ?? res.statusText ?? 'Request failed';
    throw new Error(message);
  }
  return body?.data ?? body;
}

export function unwrapBlogPage(response) {
  const data = response?.data ?? response;
  return {
    items: data?.items ?? [],
    page: data?.page ?? 0,
    size: data?.size ?? 12,
    totalElements: data?.totalElements ?? 0,
    totalPages: data?.totalPages ?? 0,
    hasNext: data?.hasNext ?? false,
  };
}

export async function listPublishedBlogs(page = 0, size = 12) {
  const data = await getJson(`/api/public/blog?page=${page}&size=${size}`);
  return unwrapBlogPage(data);
}

export async function getBlogBySlug(slug) {
  return getJson(`/api/public/blog/${encodeURIComponent(slug)}`);
}
