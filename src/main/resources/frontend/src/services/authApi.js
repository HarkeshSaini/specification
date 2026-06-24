const API_BASE = import.meta.env.VITE_API_BASE ?? '';

/**
 * POST /api/auth/login
 */
export async function login(email, password) {
  const res = await fetch(`${API_BASE}/api/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password }),
  });
  if (!res.ok) {
    let message = res.statusText;
    try {
      const body = await res.json();
      message = body?.message ?? message;
    } catch {
      /* ignore */
    }
    throw new Error(message);
  }
  return res.json();
}

/**
 * POST /api/auth/register — public website user signup
 */
export async function registerWebsiteUser(payload) {
  const res = await fetch(`${API_BASE}/api/auth/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  });
  if (!res.ok) {
    let message = res.statusText;
    try {
      const body = await res.json();
      message = body?.message ?? message;
    } catch {
      /* ignore */
    }
    throw new Error(message);
  }
  return res.json();
}

/**
 * GET /home/key-dynamic-word
 */
export async function fetchHomeListing(langCode = 'en') {
  const res = await fetch(`${API_BASE}/home/key-dynamic-word?langCode=${encodeURIComponent(langCode)}`);
  if (!res.ok) {
    throw new Error('Failed to load listings');
  }
  return res.json();
}
