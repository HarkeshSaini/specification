import { createContext, useCallback, useContext, useMemo, useState } from 'react';
import { canManageUsers, isAdmin, isAdminPortalRole, isManager, isSuperAdmin, isWebsiteUser } from '../utils/roles';

const STORAGE_KEY = 'spec_auth_session';

const AuthContext = createContext(null);

function readSession() {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

function extractAuthPayload(authResponse) {
  const payload = authResponse?.data ?? authResponse;
  return {
    token: payload?.accessToken ?? null,
    role: payload?.role ?? null,
    profileId: payload?.profileId ?? null,
    redirectPath: payload?.redirectPath ?? null,
  };
}

export function AuthProvider({ children }) {
  const [session, setSession] = useState(readSession);

  const login = useCallback((authResponse) => {
    const { token, role, profileId, redirectPath } = extractAuthPayload(authResponse);
    const next = { token, role, profileId, redirectPath, loggedInAt: Date.now() };
    localStorage.setItem(STORAGE_KEY, JSON.stringify(next));
    setSession(next);
    return next;
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem(STORAGE_KEY);
    setSession(null);
  }, []);

  const value = useMemo(
    () => ({
      session,
      role: session?.role ?? null,
      profileId: session?.profileId ?? null,
      redirectPath: session?.redirectPath ?? null,
      isAuthenticated: Boolean(session?.token),
      isAdminPortal: isAdminPortalRole(session?.role),
      isSuperAdmin: isSuperAdmin(session?.role),
      canManageUsers: canManageUsers(session?.role),
      isAdmin: isAdmin(session?.role),
      isManager: isManager(session?.role),
      isWebsiteUser: isWebsiteUser(session?.role),
      login,
      logout,
    }),
    [session, login, logout],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return ctx;
}
