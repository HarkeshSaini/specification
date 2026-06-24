import { createContext, useCallback, useContext, useMemo, useState } from 'react';

const AuthModalContext = createContext(null);

export function AuthModalProvider({ children }) {
  const [open, setOpen] = useState(false);
  const [activeTab, setActiveTab] = useState('login');

  const openLogin = useCallback(() => {
    setActiveTab('login');
    setOpen(true);
  }, []);

  const openRegister = useCallback(() => {
    setActiveTab('register');
    setOpen(true);
  }, []);

  const openForgot = useCallback(() => {
    setActiveTab('forgot');
    setOpen(true);
  }, []);

  const closeModal = useCallback(() => setOpen(false), []);

  const value = useMemo(
    () => ({
      open,
      activeTab,
      setActiveTab,
      openLogin,
      openRegister,
      openForgot,
      closeModal,
    }),
    [open, activeTab, openLogin, openRegister, openForgot, closeModal],
  );

  return <AuthModalContext.Provider value={value}>{children}</AuthModalContext.Provider>;
}

export function useAuthModal() {
  const ctx = useContext(AuthModalContext);
  if (!ctx) {
    throw new Error('useAuthModal must be used within AuthModalProvider');
  }
  return ctx;
}
