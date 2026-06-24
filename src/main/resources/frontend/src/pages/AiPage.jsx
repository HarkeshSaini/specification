import { Link } from 'react-router-dom';
import { useAuthModal } from '../context/AuthModalContext';

const CAPABILITIES = [
  'Scan project files and build rich context',
  'Generate structured change plans via LLM',
  'Apply safe code modifications automatically',
  'Chat with AI for quick specification answers',
];

export default function AiPage() {
  const { openLogin } = useAuthModal();

  return (
    <div className="site-page">
      <div className="site-page-inner">
        <p className="site-section-eyebrow">AI</p>
        <h1 className="site-page-title">AI specification assistant</h1>
        <p className="site-page-lead">
          Powered by Ollama — scan code, refactor with agents, and chat with an intelligent assistant.
        </p>

        <div className="site-ai-page-actions">
          <Link to="/ai/chat" className="btn-gradient site-ai-btn">
            Open Specification AI Chat
          </Link>
          <button type="button" onClick={openLogin} className="site-btn-outline">
            Login for full access
          </button>
        </div>

        <div className="site-ai-grid">
          <div className="site-form-card">
            <h2 className="site-card-title">Capabilities</h2>
            <ul className="site-ai-list">
              {CAPABILITIES.map((item) => (
                <li key={item}>
                  <span className="site-ai-check" aria-hidden="true">✓</span>
                  {item}
                </li>
              ))}
            </ul>
          </div>

          <div className="site-ai-terminal">
            <p className="site-ai-terminal-comment"># Chat — multi-model</p>
            <p className="site-ai-terminal-cmd">POST /api/website/ai-chat</p>
            <p className="site-ai-terminal-body">{`{ "command": "...", "model": "llama3", "auto": "true" }`}</p>
            <p className="site-ai-terminal-comment site-ai-terminal-spaced"># Agent — code changes</p>
            <p className="site-ai-terminal-cmd">POST /api/website/ai-agent</p>
            <p className="site-ai-terminal-result">→ Specification AI Chat at /ai/chat</p>
          </div>
        </div>
      </div>
    </div>
  );
}
