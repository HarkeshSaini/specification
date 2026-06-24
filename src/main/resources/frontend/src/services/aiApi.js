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

async function postAiData(path, payload) {
  const res = await fetch(`${API_BASE}${path}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      ...getAuthHeaders(),
    },
    body: JSON.stringify(payload),
  });

  let body = null;
  try {
    body = await res.json();
  } catch {
    /* ignore */
  }

  if (!res.ok) {
    const message =
      body?.data?.message ??
      body?.message ??
      body?.error?.message ??
      res.statusText ??
      'AI request failed';
    throw new Error(message);
  }

  return body?.data ?? body;
}

async function postAi(path, payload) {
  const data = await postAiData(path, payload);
  return data?.response ?? data?.modifiedDetail ?? '';
}

/**
 * POST /api/website/ai-chat
 */
export function sendChatMessage({ command, model, agent, auto }) {
  return postAi('/api/website/ai-chat', {
    command,
    model: model && model !== 'auto' ? model : undefined,
    agent: agent && agent !== 'auto' ? agent : undefined,
    auto: auto ? 'true' : undefined,
  });
}

/**
 * POST /api/website/ai-agent
 */
export function sendAgentMessage({ command, model, filePath, agent, auto }) {
  return postAi('/api/website/ai-agent', {
    command,
    model: model && model !== 'auto' ? model : undefined,
    filePath,
    agent: agent && agent !== 'auto' ? agent : undefined,
    auto: auto ? 'true' : undefined,
  });
}

/**
 * POST /api/website/ai-agent/batch — parallel bulkhead + CompletableFuture
 */
export async function sendAgentBatchMessage({ command, model, filePaths, agent, auto }) {
  const data = await postAiData('/api/website/ai-agent/batch', {
    command,
    model: model && model !== 'auto' ? model : undefined,
    filePaths,
    agent: agent && agent !== 'auto' ? agent : undefined,
    auto: auto ? 'true' : undefined,
  });
  const results = data?.results ?? [];
  return results
    .map((r) =>
      r.success
        ? `**${r.filePath}**\n${r.response || 'Done.'}`
        : `**${r.filePath}**\nError: ${r.error || 'Failed'}`,
    )
    .join('\n\n');
}
/**
 * GET /api/website/ai-provider
 */
export async function fetchAiProvider() {
  const res = await fetch(`${API_BASE}/api/website/ai-provider`, {
    headers: getAuthHeaders(),
  });
  let body = null;
  try {
    body = await res.json();
  } catch {
    /* ignore */
  }
  if (!res.ok) {
    return null;
  }
  return body?.data ?? body;
}

export async function fetchAiModels() {
  const res = await fetch(`${API_BASE}/api/website/ai-models`, {
    headers: getAuthHeaders(),
  });
  let body = null;
  try {
    body = await res.json();
  } catch {
    /* ignore */
  }
  if (!res.ok) {
    return null;
  }
  const data = body?.data ?? body;
  return Array.isArray(data) ? data : null;
}
