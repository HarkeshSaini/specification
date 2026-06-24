export default function AiChatSidebar({
  chats,
  activeId,
  open,
  onClose,
  onNewChat,
  onSelectChat,
  onDeleteChat,
}) {
  return (
    <>
      {open ? <button type="button" className="ai-chat-sidebar-overlay" onClick={onClose} aria-label="Close sidebar" /> : null}

      <aside className={`ai-chat-sidebar ${open ? 'ai-chat-sidebar-open' : ''}`} aria-label="Chat history">
        <div className="ai-chat-sidebar-head">
          <p className="ai-chat-sidebar-title">Specification</p>
          <button type="button" className="ai-chat-sidebar-close" onClick={onClose} aria-label="Close">
            ✕
          </button>
        </div>

        <button type="button" className="ai-chat-new-btn" onClick={onNewChat}>
          + New chat
        </button>

        <nav className="ai-chat-history">
          {chats.length === 0 ? (
            <p className="ai-chat-history-empty">No conversations yet</p>
          ) : (
            chats.map((chat) => (
              <div key={chat.id} className={`ai-chat-history-item ${chat.id === activeId ? 'ai-chat-history-item-active' : ''}`}>
                <button type="button" className="ai-chat-history-btn" onClick={() => onSelectChat(chat.id)}>
                  <span className="ai-chat-history-title">{chat.title}</span>
                  <span className="ai-chat-history-meta">{chat.messages.length} messages</span>
                </button>
                <button
                  type="button"
                  className="ai-chat-history-delete"
                  onClick={() => onDeleteChat(chat.id)}
                  aria-label={`Delete ${chat.title}`}
                >
                  🗑
                </button>
              </div>
            ))
          )}
        </nav>
      </aside>
    </>
  );
}
