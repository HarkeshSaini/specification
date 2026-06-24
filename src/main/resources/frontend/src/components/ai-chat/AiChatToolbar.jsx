function providerLabel(status) {
  if (!status) return 'Checking provider…';
  if (status.activeProvider === 'NONE') return 'No AI provider';
  if (status.activeProvider === 'OLLAMA') return 'Ollama';
  if (status.activeProvider === 'HUGGING_FACE') {
    return status.huggingFaceFreeMode ? 'HuggingFace (free)' : 'HuggingFace';
  }
  return status.activeProvider ?? 'Unknown';
}

export default function AiChatToolbar({ providerStatus }) {
  const label = providerLabel(providerStatus);
  const isFallback =
    providerStatus?.activeProvider === 'HUGGING_FACE' && providerStatus?.ollamaAvailable === false;

  return (
    <div className="ai-chat-toolbar">
      <div className="ai-chat-toolbar-brand">
        <span className="ai-chat-toolbar-logo">S</span>
        <div>
          <p className="ai-chat-toolbar-name">Specification</p>
          <p className="ai-chat-toolbar-tagline">
            Multi-model · Multi-agent · Auto
            {providerStatus ? (
              <>
                {' · '}
                <span
                  className={`ai-chat-provider-badge${isFallback ? ' ai-chat-provider-badge--fallback' : ''}`}
                  title={
                    providerStatus.activeProvider === 'NONE'
                      ? 'Start Ollama (ollama serve) — HuggingFace is blocked or unreachable on this network'
                      : isFallback
                        ? providerStatus.huggingFaceFreeMode
                          ? 'Ollama unavailable — using HuggingFace free tier (no API key)'
                          : 'Ollama unavailable — using HuggingFace fallback'
                        : `Active LLM provider: ${label}`
                  }
                >
                  {label}
                </span>
              </>
            ) : null}
          </p>
        </div>
      </div>
    </div>
  );
}
