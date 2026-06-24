import { createContext, useCallback, useContext, useLayoutEffect, useMemo, useState } from 'react';

const STORAGE_KEY = 'spec_theme';

export const THEMES = {
  dark: { id: 'dark', label: 'Dark', icon: 'moon' },
  light: { id: 'light', label: 'Light', icon: 'sun' },
  ocean: { id: 'ocean', label: 'Ocean', icon: 'wave' },
};

const ThemeContext = createContext(null);

function readTheme() {
  try {
    const saved = localStorage.getItem(STORAGE_KEY);
    if (saved && THEMES[saved]) {
      applyTheme(saved);
      return saved;
    }
  } catch {
    /* ignore */
  }
  applyTheme('dark');
  return 'dark';
}

function applyTheme(theme) {
  document.documentElement.setAttribute('data-theme', theme);
  const meta = document.querySelector('meta[name="theme-color"]');
  if (meta) {
    const colors = { dark: '#030712', light: '#f1f5f9', ocean: '#0c1929' };
    meta.setAttribute('content', colors[theme] ?? colors.dark);
  }
}

export function ThemeProvider({ children }) {
  const [theme, setThemeState] = useState(readTheme);

  useLayoutEffect(() => {
    applyTheme(theme);
  }, [theme]);

  const setTheme = useCallback((next) => {
    if (!THEMES[next]) return;
    localStorage.setItem(STORAGE_KEY, next);
    setThemeState(next);
    applyTheme(next);
  }, []);

  const value = useMemo(() => ({ theme, setTheme, themes: Object.values(THEMES) }), [theme, setTheme]);

  return <ThemeContext.Provider value={value}>{children}</ThemeContext.Provider>;
}

export function useTheme() {
  const ctx = useContext(ThemeContext);
  if (!ctx) throw new Error('useTheme must be used within ThemeProvider');
  return ctx;
}
