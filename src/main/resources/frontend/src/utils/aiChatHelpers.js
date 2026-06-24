const AGENT_KEYWORDS = /\b(refactor|modify|scan|file|code|implement|fix bug|apply changes)\b/i;
const FILE_PATH_PATTERN = /(?:^|\s)([\w./\\-]+\.(java|js|jsx|ts|tsx|py|go|rs|xml|yaml|yml|json|md|css|html))(?:\s|$)/i;

export function resolveAgentMode(agentId, message, filePaths) {
  const hasFiles = Array.isArray(filePaths)
    ? filePaths.length > 0
    : Boolean(filePaths?.trim?.());
  if (agentId === 'code-agent') return 'agent';
  if (agentId === 'chat' || agentId === 'spec-writer') return 'chat';
  if (hasFiles) return 'agent';
  if (AGENT_KEYWORDS.test(message) || FILE_PATH_PATTERN.test(message)) return 'agent';
  return 'chat';
}

export function resolveFilePaths(selectedFiles, message) {
  if (Array.isArray(selectedFiles) && selectedFiles.length > 0) {
    return selectedFiles;
  }
  const fromMessage = extractFilePath(message);
  return fromMessage ? [fromMessage] : [];
}

export function extractFilePath(message) {
  const match = message.match(FILE_PATH_PATTERN);
  return match?.[1] ?? '';
}

export function buildPrompt(agentId, message) {
  if (agentId === 'spec-writer') {
    return `You are a specification writer for the Specification platform. Produce clear, structured output with sections where helpful.\n\nUser request: ${message}`;
  }
  return message;
}

export function titleFromMessage(text) {
  const trimmed = text.trim().replace(/\s+/g, ' ');
  if (!trimmed) return 'New chat';
  return trimmed.length > 42 ? `${trimmed.slice(0, 42)}…` : trimmed;
}

export function createId() {
  return `${Date.now()}-${Math.random().toString(36).slice(2, 9)}`;
}

const STORAGE_KEY = 'spec_ai_chats';

export function loadChats() {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    return raw ? JSON.parse(raw) : [];
  } catch {
    return [];
  }
}

export function saveChats(chats) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(chats));
}
