export const AI_MODELS_FALLBACK = [
  { id: 'auto', label: 'Auto model', description: 'Server picks the best available model' },
  { id: 'llama3', label: 'Llama 3', description: 'General-purpose reasoning' },
  { id: 'mistral', label: 'Mistral', description: 'Fast, balanced responses' },
  { id: 'codellama', label: 'Code Llama', description: 'Code and specification tasks' },
  { id: 'phi3', label: 'Phi-3', description: 'Lightweight, quick answers' },
];

/** @deprecated use AI_MODELS_FALLBACK or dynamic models from fetchAiModels */
export const AI_MODELS = AI_MODELS_FALLBACK;

export const AI_AGENTS = [
  { id: 'auto', label: 'Auto', description: 'Routes to chat or code agent automatically' },
  { id: 'chat', label: 'Specification Chat', description: 'General Q&A and spec writing' },
  { id: 'spec-writer', label: 'Spec Writer', description: 'Structured documentation & requirements' },
  { id: 'code-agent', label: 'Code Agent', description: 'Scan and modify project files' },
];

export const STARTER_PROMPTS = [
  'Draft an OpenAPI spec for a user login endpoint',
  'Explain how JWT authentication works in our platform',
  'Refactor the selected file to improve readability',
  'Summarize best practices for API versioning',
];

export const WELCOME_MESSAGE = {
  role: 'assistant',
  content:
    'Welcome to **Specification AI**. Uses **Ollama** when running locally; if Ollama is off, falls back to **HuggingFace free tier** (no API key required). Optional `HF_API_KEY` unlocks more models. Choose model/agent or leave both on **Auto**.',
};
