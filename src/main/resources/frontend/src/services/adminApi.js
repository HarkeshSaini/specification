const API_BASE = import.meta.env.VITE_API_BASE ?? '';

function getAuthHeaders() {
  try {
    const raw = localStorage.getItem('spec_auth_session');
    const session = raw ? JSON.parse(raw) : null;
    if (session?.token) {
      return { Authorization: `Bearer ${session.token}` };
    }
  } catch {
    /* ignore */
  }
  return {};
}

async function request(path, options = {}) {
  const res = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...getAuthHeaders(),
      ...options.headers,
    },
  });
  if (!res.ok) {
    let message = res.statusText;
    try {
      const body = await res.json();
      message = body?.message ?? body?.data?.message ?? message;
    } catch {
      /* ignore */
    }
    throw new Error(message || 'Request failed');
  }
  if (res.status === 204) return null;
  return res.json();
}

export function unwrapData(response) {
  return response?.data ?? response;
}

export function unwrapPage(response) {
  const data = unwrapData(response);
  return {
    items: data?.items ?? [],
    page: data?.page ?? 0,
    size: data?.size ?? 10,
    totalElements: data?.totalElements ?? 0,
    totalPages: data?.totalPages ?? 0,
    hasNext: data?.hasNext ?? false,
  };
}

export const fetchSuperAdminProfile = () => request('/api/super-admin/me');
export const fetchAdminProfile = () => request('/api/admin/me');
export const fetchManagerProfile = () => request('/api/manager/me');
export const fetchWebsiteProfile = () => request('/api/website/me');

export async function fetchManagerWebsiteUsers() {
  const res = await request('/api/manager/website-users');
  const data = unwrapData(res);
  if (Array.isArray(data)) return data;
  if (Array.isArray(res)) return res.map((item) => unwrapData(item));
  return data ? [data] : [];
}

export const listSuperAdmins = (page = 0, size = 10) =>
  request(`/api/super-admin/getAllSuperAdmin?page=${page}&size=${size}`);

export const createSuperAdmin = (payload) =>
  request('/api/super-admin/createSuperAdmin', { method: 'POST', body: JSON.stringify(payload) });
export const updateSuperAdmin = (userId, payload) =>
  request(`/api/super-admin/updateById/${userId}`, { method: 'PUT', body: JSON.stringify(payload) });
export const getSuperAdminById = (userId) => request(`/api/super-admin/findById/${userId}`);
export const deleteSuperAdmin = (userId) =>
  request(`/api/super-admin/deleteById/${userId}`, { method: 'DELETE' });

export const listAdmins = (page = 0, size = 10) =>
  request(`/api/admin/getAllAdmin?page=${page}&size=${size}`);
export const listManagers = (page = 0, size = 10) =>
  request(`/api/managers/getAllManagers?page=${page}&size=${size}`);
export const listWebsiteUsers = (page = 0, size = 10) =>
  request(`/api/webUser/getAllUser?page=${page}&size=${size}`);

export const createAdmin = (payload) =>
  request('/api/admin/createAdmin', { method: 'POST', body: JSON.stringify(payload) });
export const updateAdmin = (userId, payload) =>
  request(`/api/admin/updateById/${userId}`, { method: 'PUT', body: JSON.stringify(payload) });
export const getAdminById = (userId) => request(`/api/admin/findById/${userId}`);
export const deleteAdmin = (userId) => request(`/api/admin/deleteById/${userId}`, { method: 'DELETE' });

export const createManager = (payload) =>
  request('/api/managers/createNewManagers', { method: 'POST', body: JSON.stringify(payload) });
export const updateManager = (userId, payload) =>
  request(`/api/managers/updateManagersById/${userId}`, { method: 'PUT', body: JSON.stringify(payload) });
export const getManagerById = (userId) => request(`/api/managers/getManagersById/${userId}`);
export const deleteManager = (userId) =>
  request(`/api/managers/deleteManagersById/${userId}`, { method: 'DELETE' });

export const createWebsiteUser = (payload) =>
  request('/api/webUser/createNewUser', { method: 'POST', body: JSON.stringify(payload) });
export const updateWebsiteUser = (userId, payload) =>
  request(`/api/webUser/updateUserById/${userId}`, { method: 'PUT', body: JSON.stringify(payload) });
export const getWebsiteUserById = (userId) => request(`/api/webUser/getUserById/${userId}`);
export const deleteWebsiteUser = (userId) =>
  request(`/api/webUser/deleteUserById/${userId}`, { method: 'DELETE' });

export const listBlogs = (page = 0, size = 10) =>
  request(`/api/admin/blog?page=${page}&size=${size}`);
export const createBlog = (payload) =>
  request('/api/admin/blog', { method: 'POST', body: JSON.stringify(payload) });
export const updateBlog = (blogId, payload) =>
  request(`/api/admin/blog/${blogId}`, { method: 'PUT', body: JSON.stringify(payload) });
export const getBlogById = (blogId) => request(`/api/admin/blog/${blogId}`);
export const deleteBlog = (blogId) => request(`/api/admin/blog/${blogId}`, { method: 'DELETE' });
