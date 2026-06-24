function formatTime(ts) {
  try {
    return new Intl.DateTimeFormat(undefined, { hour: 'numeric', minute: '2-digit' }).format(new Date(ts));
  } catch {
    return '';
  }
}

function renderContent(text) {
  if (!text) return null;
  const parts = text.split(/(\*\*[^*]+\*\*)/g);
  return parts.map((part, i) => {
    if (part.startsWith('**') && part.endsWith('**')) {
      return <strong key={i}>{part.slice(2, -2)}</strong>;
    }
    return part.split('\n').map((line, j, arr) => (
      <span key={`${i}-${j}`}>
        {line}
        {j < arr.length - 1 ? <br /> : null}
      </span>
    ));
  });
}

export default function AiChatMessageList({ messages, loading }) {
  return (
    <div className="ai-chat-messages" role="log" aria-live="polite" aria-relevant="additions">
      {messages.map((msg) => (
        <article key={msg.id} className={`ai-chat-message ai-chat-message-${msg.role}`}>
          <div className="ai-chat-message-avatar" aria-hidden="true">
            {msg.role === 'assistant' ? 'S' : 'You'}
          </div>
          <div className="ai-chat-message-body">
            <header className="ai-chat-message-meta">
              <span className="ai-chat-message-author">
                {msg.role === 'assistant' ? 'Specification' : 'You'}
              </span>
              {msg.meta?.mode ? (
                <span className="ai-chat-message-badge">{msg.meta.mode}</span>
              ) : null}
              <time className="ai-chat-message-time">{formatTime(msg.timestamp)}</time>
            </header>
            <div className="ai-chat-message-content">{renderContent(msg.content)}</div>
          </div>
        </article>
      ))}

      {loading ? (
        <article className="ai-chat-message ai-chat-message-assistant ai-chat-message-loading">
          <div className="ai-chat-message-avatar" aria-hidden="true">S</div>
          <div className="ai-chat-message-body">
            <header className="ai-chat-message-meta">
              <span className="ai-chat-message-author">Specification</span>
            </header>
            <div className="ai-chat-typing" aria-label="Specification is thinking">
              <span />
              <span />
              <span />
            </div>
          </div>
        </article>
      ) : null}
    </div>
  );
}
