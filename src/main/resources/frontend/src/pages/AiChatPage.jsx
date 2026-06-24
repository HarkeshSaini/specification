import { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import AiChatComposer from '../components/ai-chat/AiChatComposer';
import AiChatMessageList from '../components/ai-chat/AiChatMessageList';
import AiChatSidebar from '../components/ai-chat/AiChatSidebar';
import AiChatToolbar from '../components/ai-chat/AiChatToolbar';
import { STARTER_PROMPTS, WELCOME_MESSAGE, AI_MODELS_FALLBACK } from '../config/aiChatConfig';
import { useAuth } from '../context/AuthContext';
import { useAuthModal } from '../context/AuthModalContext';
import { fetchAiModels, fetchAiProvider, sendAgentBatchMessage, sendAgentMessage, sendChatMessage } from '../services/aiApi';
import {
  createId,
  loadChats,
  resolveAgentMode,
  resolveFilePaths,
  saveChats,
  titleFromMessage,
} from '../utils/aiChatHelpers';

function toModelOptions(names, providerStatus) {
  const auto = AI_MODELS_FALLBACK[0];
  if (!names?.length) return AI_MODELS_FALLBACK;
  const provider =
    providerStatus?.activeProvider === 'HUGGING_FACE' ? 'HuggingFace' : 'Ollama';
  const seen = new Set();
  const options = [auto];
  for (const name of names) {
    if (!name || seen.has(name)) continue;
    seen.add(name);
    options.push({
      id: name,
      label: name,
      description: `${provider} model (LangChain4j): ${name}`,
    });
  }
  return options.length > 1 ? options : AI_MODELS_FALLBACK;
}

function createWelcomeMessages() {
  return [{ ...WELCOME_MESSAGE, id: createId(), timestamp: Date.now() }];
}

function createNewChat() {
  return {
    id: createId(),
    title: 'New chat',
    createdAt: Date.now(),
    messages: createWelcomeMessages(),
  };
}

export default function AiChatPage() {
  const { isAuthenticated } = useAuth();
  const { openLogin } = useAuthModal();
  const scrollRef = useRef(null);

  const [chats, setChats] = useState(() => loadChats());
  const [activeId, setActiveId] = useState(() => loadChats()[0]?.id ?? null);
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);
  const [model, setModel] = useState('auto');
  const [agent, setAgent] = useState('auto');
  const [selectedFiles, setSelectedFiles] = useState([]);
  const [modelOptions, setModelOptions] = useState(AI_MODELS_FALLBACK);
  const [providerStatus, setProviderStatus] = useState(null);

  const activeChat = useMemo(
    () => chats.find((c) => c.id === activeId) ?? null,
    [chats, activeId],
  );

  const showFilePicker = agent === 'code-agent';

  useEffect(() => {
    if (!isAuthenticated) return undefined;
    let cancelled = false;
    Promise.all([fetchAiModels(), fetchAiProvider()]).then(([names, provider]) => {
      if (cancelled) return;
      if (provider) setProviderStatus(provider);
      if (names) setModelOptions(toModelOptions(names, provider));
    });
    return () => {
      cancelled = true;
    };
  }, [isAuthenticated]);

  useEffect(() => {
    if (chats.length === 0) {
      const chat = createNewChat();
      setChats([chat]);
      setActiveId(chat.id);
    } else if (!activeId) {
      setActiveId(chats[0].id);
    }
  }, [chats.length, activeId]);

  useEffect(() => {
    saveChats(chats);
  }, [chats]);

  useEffect(() => {
    const el = scrollRef.current;
    if (el) el.scrollTop = el.scrollHeight;
  }, [activeChat?.messages, loading]);

  useEffect(() => {
    if (!sidebarOpen) return undefined;
    const prev = document.body.style.overflow;
    document.body.style.overflow = 'hidden';
    return () => {
      document.body.style.overflow = prev;
    };
  }, [sidebarOpen]);

  const updateChat = useCallback((chatId, updater) => {
    setChats((prev) => prev.map((c) => (c.id === chatId ? updater(c) : c)));
  }, []);

  function handleNewChat() {
    const chat = createNewChat();
    setChats((prev) => [chat, ...prev]);
    setActiveId(chat.id);
    setInput('');
    setSidebarOpen(false);
  }

  function handleSelectChat(id) {
    setActiveId(id);
    setSidebarOpen(false);
  }

  function handleDeleteChat(id) {
    setChats((prev) => {
      const next = prev.filter((c) => c.id !== id);
      if (activeId === id) {
        setActiveId(next[0]?.id ?? null);
      }
      if (next.length === 0) {
        const chat = createNewChat();
        setActiveId(chat.id);
        return [chat];
      }
      return next;
    });
  }

  function handleAgentChange(value) {
    setAgent(value);
    if (value !== 'code-agent') {
      setSelectedFiles([]);
    }
  }

  function handleAddFiles(paths) {
    setSelectedFiles((prev) => {
      const next = [...prev];
      for (const path of paths) {
        if (path && !next.includes(path)) next.push(path);
      }
      return next;
    });
  }

  function handleRemoveFile(path) {
    setSelectedFiles((prev) => prev.filter((p) => p !== path));
  }

  async function handleSend() {
    const text = input.trim();
    if (!text || loading || !activeChat) return;

    if (!isAuthenticated) {
      openLogin();
      return;
    }

    const userMessage = {
      id: createId(),
      role: 'user',
      content: text,
      timestamp: Date.now(),
    };

    const chatId = activeChat.id;
    const isFirstUserMessage = activeChat.messages.filter((m) => m.role === 'user').length === 0;

    updateChat(chatId, (c) => ({
      ...c,
      title: isFirstUserMessage ? titleFromMessage(text) : c.title,
      messages: [...c.messages, userMessage],
    }));
    setInput('');
    setLoading(true);

    const filePaths = resolveFilePaths(selectedFiles, text);
    const mode = resolveAgentMode(agent, text, filePaths);
    const effectiveAgent = mode === 'agent' ? 'code-agent' : agent;
    const modeLabel = mode === 'agent' ? 'Code Agent' : agent === 'spec-writer' ? 'Spec Writer' : 'Chat';

    try {
      let reply;
      if (mode === 'agent') {
        if (filePaths.length === 0) {
          throw new Error('Code Agent requires at least one file. Use the file icon or + to select files.');
        }
        if (filePaths.length === 1) {
          reply = await sendAgentMessage({
            command: text,
            model,
            filePath: filePaths[0],
            agent: effectiveAgent,
            auto: agent === 'auto' || model === 'auto',
          });
        } else {
          reply = await sendAgentBatchMessage({
            command: text,
            model,
            filePaths,
            agent: effectiveAgent,
            auto: agent === 'auto' || model === 'auto',
          });
        }
      } else {
        reply = await sendChatMessage({
          command: text,
          model,
          agent: effectiveAgent,
          auto: agent === 'auto' || model === 'auto',
        });
      }

      updateChat(chatId, (c) => ({
        ...c,
        messages: [
          ...c.messages,
          {
            id: createId(),
            role: 'assistant',
            content: reply || 'No response received.',
            timestamp: Date.now(),
            meta: { mode: modeLabel, model: model === 'auto' ? 'auto' : model },
          },
        ],
      }));
    } catch (err) {
      updateChat(chatId, (c) => ({
        ...c,
        messages: [
          ...c.messages,
          {
            id: createId(),
            role: 'assistant',
            content: `Sorry, something went wrong: ${err.message}`,
            timestamp: Date.now(),
            meta: { mode: 'Error' },
          },
        ],
      }));
    } finally {
      setLoading(false);
    }
  }

  function handleStarterPrompt(prompt) {
    setInput(prompt);
  }

  const showStarters = activeChat && activeChat.messages.filter((m) => m.role === 'user').length === 0;

  return (
    <div className="ai-chat-page">
      <AiChatSidebar
        chats={chats}
        activeId={activeId}
        open={sidebarOpen}
        onClose={() => setSidebarOpen(false)}
        onNewChat={handleNewChat}
        onSelectChat={handleSelectChat}
        onDeleteChat={handleDeleteChat}
      />

      <div className="ai-chat-main">
        <div className="ai-chat-main-top">
          <button
            type="button"
            className="ai-chat-menu-btn"
            onClick={() => setSidebarOpen(true)}
            aria-label="Open chat history"
          >
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M4 6h16M4 12h16M4 18h16" />
            </svg>
          </button>

          <AiChatToolbar providerStatus={providerStatus} />
        </div>

        {!isAuthenticated ? (
          <div className="ai-chat-auth-banner">
            <p>Sign in to chat with Specification AI using your workspace models and agents.</p>
            <button type="button" className="btn-gradient ai-chat-auth-btn" onClick={openLogin}>
              Login to chat
            </button>
          </div>
        ) : null}

        <div className="ai-chat-scroll" ref={scrollRef}>
          {activeChat ? <AiChatMessageList messages={activeChat.messages} loading={loading} /> : null}

          {showStarters ? (
            <div className="ai-chat-starters">
              <p className="ai-chat-starters-label">Try asking</p>
              <div className="ai-chat-starters-grid">
                {STARTER_PROMPTS.map((prompt) => (
                  <button
                    key={prompt}
                    type="button"
                    className="ai-chat-starter-card"
                    onClick={() => handleStarterPrompt(prompt)}
                  >
                    {prompt}
                  </button>
                ))}
              </div>
            </div>
          ) : null}
        </div>

        <AiChatComposer
          value={input}
          onChange={setInput}
          onSubmit={handleSend}
          disabled={loading}
          placeholder="Message Specification…"
          model={model}
          agent={agent}
          selectedFiles={selectedFiles}
          onModelChange={setModel}
          onAgentChange={handleAgentChange}
          onAddFiles={handleAddFiles}
          onRemoveFile={handleRemoveFile}
          showFilePicker={showFilePicker}
          modelOptions={modelOptions}
        />
      </div>
    </div>
  );
}
