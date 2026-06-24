import { useEffect, useRef } from 'react';
import { AI_AGENTS, AI_MODELS_FALLBACK } from '../../config/aiChatConfig';

function shortOptionLabel(opt) {
  if (opt.id === 'auto') return 'Auto';
  const label = opt.label ?? opt.id;
  return label.length > 14 ? opt.id : label;
}

function fileNameFromPath(path) {
  const parts = path.replace(/\\/g, '/').split('/');
  return parts[parts.length - 1] || path;
}

function CompactSelect({ value, onChange, options, ariaLabel }) {
  return (
    <select
      value={value}
      onChange={(e) => onChange(e.target.value)}
      className="ai-chat-select ai-chat-select--compact"
      aria-label={ariaLabel}
      title={options.find((o) => o.id === value)?.description}
    >
      {options.map((opt) => (
        <option key={opt.id} value={opt.id} title={opt.description}>
          {shortOptionLabel(opt)}
        </option>
      ))}
    </select>
  );
}

function PlusIcon() {
  return (
    <svg className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
      <path strokeLinecap="round" strokeLinejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
    </svg>
  );
}

function FileIcon() {
  return (
    <svg className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
      <path
        strokeLinecap="round"
        strokeLinejoin="round"
        d="M19.5 14.25v-2.625a3.375 3.375 0 00-3.375-3.375h-1.5A1.125 1.125 0 0113.5 7.125V5.625a3.375 3.375 0 00-3.375-3.375H8.25m2.25 0H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 00-9-9z"
      />
    </svg>
  );
}

export default function AiChatComposer({
  value,
  onChange,
  onSubmit,
  disabled,
  placeholder,
  model,
  agent,
  selectedFiles = [],
  onModelChange,
  onAgentChange,
  onAddFiles,
  onRemoveFile,
  showFilePicker,
  modelOptions = AI_MODELS_FALLBACK,
}) {
  const textareaRef = useRef(null);
  const fileInputRef = useRef(null);

  useEffect(() => {
    const el = textareaRef.current;
    if (!el) return;
    el.style.height = 'auto';
    el.style.height = `${Math.min(el.scrollHeight, 200)}px`;
  }, [value]);

  function handleKeyDown(e) {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      if (!disabled && value.trim()) onSubmit();
    }
  }

  function openFilePicker() {
    fileInputRef.current?.click();
  }

  function handleFileChange(e) {
    const list = e.target.files;
    if (!list?.length) return;
    const paths = Array.from(list).map((f) => f.webkitRelativePath || f.name);
    onAddFiles(paths);
    e.target.value = '';
  }

  return (
    <div className="ai-chat-composer">
      <div className="ai-chat-composer-box">
        <div className="ai-chat-composer-input-row">
          {showFilePicker ? (
            <div className="ai-chat-file-attach">
              <input
                ref={fileInputRef}
                type="file"
                multiple
                className="ai-chat-file-input-hidden"
                onChange={handleFileChange}
                aria-hidden
                tabIndex={-1}
              />
              <button
                type="button"
                className="ai-chat-file-icon-btn"
                onClick={openFilePicker}
                disabled={disabled}
                aria-label="Select files"
                title="Select files"
              >
                <FileIcon />
              </button>
              <button
                type="button"
                className="ai-chat-file-icon-btn ai-chat-file-icon-btn--plus"
                onClick={openFilePicker}
                disabled={disabled}
                aria-label="Add more files"
                title="Add more files"
              >
                <PlusIcon />
              </button>
              {selectedFiles.length > 0 ? (
                <div className="ai-chat-file-chips">
                  {selectedFiles.map((path) => (
                    <span key={path} className="ai-chat-file-chip" title={path}>
                      <span className="ai-chat-file-chip-name">{fileNameFromPath(path)}</span>
                      <button
                        type="button"
                        className="ai-chat-file-chip-remove"
                        onClick={() => onRemoveFile(path)}
                        disabled={disabled}
                        aria-label={`Remove ${fileNameFromPath(path)}`}
                      >
                        ×
                      </button>
                    </span>
                  ))}
                </div>
              ) : null}
            </div>
          ) : null}

          <textarea
            ref={textareaRef}
            rows={1}
            value={value}
            onChange={(e) => onChange(e.target.value)}
            onKeyDown={handleKeyDown}
            placeholder={placeholder}
            disabled={disabled}
            className="ai-chat-composer-input"
            aria-label="Message Specification"
          />
          <div className="ai-chat-composer-controls">
            <CompactSelect
              value={model}
              onChange={onModelChange}
              options={modelOptions}
              ariaLabel="Model"
            />
            <CompactSelect
              value={agent}
              onChange={onAgentChange}
              options={AI_AGENTS}
              ariaLabel="Agent"
            />
            <button
              type="button"
              onClick={onSubmit}
              disabled={disabled || !value.trim()}
              className="ai-chat-composer-send"
              aria-label="Send message"
            >
              <svg className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M6 12L3.269 3.126A59.768 59.768 0 0121.485 12 59.77 59.77 0 013.27 20.876L5.999 12zm0 0h7.5" />
              </svg>
            </button>
          </div>
        </div>
      </div>
      <p className="ai-chat-composer-hint">
        Enter to send · Shift+Enter for new line
        {showFilePicker ? ' · Use + or file icon to attach multiple files' : ' · Auto mode picks model & agent for you'}
      </p>
    </div>
  );
}
