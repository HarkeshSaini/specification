import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { AuthModalProvider } from './context/AuthModalContext';
import { ThemeProvider } from './context/ThemeContext';
import ProtectedRoute from './components/auth/ProtectedRoute';
import DashboardLayout from './components/layout/DashboardLayout';
import MainLayout from './components/layout/MainLayout';
import AiChatLayout from './components/layout/AiChatLayout';
import AiPage from './pages/AiPage';
import AiChatPage from './pages/AiChatPage';
import BlogPage from './pages/BlogPage';
import BlogDetailPage from './pages/BlogDetailPage';
import ContactPage from './pages/ContactPage';
import AdminActivityPage from './pages/dashboard/admin/AdminActivityPage';
import AdminAllUsersPage from './pages/dashboard/admin/AdminAllUsersPage';
import AdminAnalyticsPage from './pages/dashboard/admin/AdminAnalyticsPage';
import AdminManagersPage from './pages/dashboard/admin/AdminManagersPage';
import AdminOverview from './pages/dashboard/admin/AdminOverview';
import AdminRolesPage from './pages/dashboard/admin/AdminRolesPage';
import AdminSettingsPage from './pages/dashboard/admin/AdminSettingsPage';
import AdminUsersPage from './pages/dashboard/admin/AdminUsersPage';
import SuperAdminUsersPage from './pages/dashboard/admin/SuperAdminUsersPage';
import {
  AdminUserFormPage,
  ManagerUserFormPage,
  SuperAdminUserFormPage,
  WebsiteUserFormPage,
} from './pages/dashboard/admin/UserFormPages';
import AdminWebsiteUsersPage from './pages/dashboard/admin/AdminWebsiteUsersPage';
import AdminBlogPage from './pages/dashboard/admin/AdminBlogPage';
import AdminBlogFormPage from './pages/dashboard/admin/AdminBlogFormPage';
import ManagerOverview from './pages/dashboard/manager/ManagerOverview';
import ManagerReportsPage from './pages/dashboard/manager/ManagerReportsPage';
import ManagerSettingsPage from './pages/dashboard/manager/ManagerSettingsPage';
import ManagerTeamPage from './pages/dashboard/manager/ManagerTeamPage';
import ManagerUsersPage from './pages/dashboard/manager/ManagerUsersPage';
import UserActivityPage from './pages/dashboard/user/UserActivityPage';
import UserOverview from './pages/dashboard/user/UserOverview';
import UserPreferencesPage from './pages/dashboard/user/UserPreferencesPage';
import UserProfilePage from './pages/dashboard/user/UserProfilePage';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import { ROLES } from './utils/roles';

export default function App() {
  return (
    <BrowserRouter>
      <ThemeProvider>
        <AuthProvider>
          <AuthModalProvider>
            <Routes>
              <Route element={<MainLayout />}>
                <Route index element={<HomePage />} />
                <Route path="blog" element={<BlogPage />} />
                <Route path="blog/:slug" element={<BlogDetailPage />} />
                <Route path="ai" element={<AiPage />} />
                <Route path="contact" element={<ContactPage />} />
              </Route>

              <Route element={<AiChatLayout />}>
                <Route path="ai/chat" element={<AiChatPage />} />
              </Route>

              <Route path="login" element={<LoginPage />} />

              <Route
                path="admin/*"
                element={
                  <ProtectedRoute allowedRoles={[ROLES.SUPER_ADMIN, ROLES.ADMIN]}>
                    <DashboardLayout portalTitle="Admin Portal" />
                  </ProtectedRoute>
                }
              >
                <Route path="dashboard" element={<AdminOverview />} />
                <Route path="users/all" element={<AdminAllUsersPage />} />
                <Route path="super-admins/new" element={<SuperAdminUserFormPage />} />
                <Route path="super-admins/:userId/edit" element={<SuperAdminUserFormPage />} />
                <Route path="super-admins" element={<SuperAdminUsersPage />} />
                <Route path="admins/new" element={<AdminUserFormPage />} />
                <Route path="admins/:userId/edit" element={<AdminUserFormPage />} />
                <Route path="admins" element={<AdminUsersPage />} />
                <Route path="managers/new" element={<ManagerUserFormPage />} />
                <Route path="managers/:userId/edit" element={<ManagerUserFormPage />} />
                <Route path="managers" element={<AdminManagersPage />} />
                <Route path="users/new" element={<WebsiteUserFormPage />} />
                <Route path="users/:userId/edit" element={<WebsiteUserFormPage />} />
                <Route path="users" element={<AdminWebsiteUsersPage />} />
                <Route path="blog/new" element={<AdminBlogFormPage />} />
                <Route path="blog/:blogId/edit" element={<AdminBlogFormPage />} />
                <Route path="blog" element={<AdminBlogPage />} />
                <Route path="analytics" element={<AdminAnalyticsPage />} />
                <Route path="activity" element={<AdminActivityPage />} />
                <Route path="roles" element={<AdminRolesPage />} />
                <Route path="settings" element={<AdminSettingsPage />} />
                <Route index element={<Navigate to="dashboard" replace />} />
              </Route>

              <Route
                path="manager/*"
                element={
                  <ProtectedRoute allowedRoles={[ROLES.MANAGER, ROLES.ADMIN, ROLES.SUPER_ADMIN]}>
                    <DashboardLayout portalTitle="Manager Portal" />
                  </ProtectedRoute>
                }
              >
                <Route path="dashboard" element={<ManagerOverview />} />
                <Route path="users" element={<ManagerUsersPage />} />
                <Route path="team" element={<ManagerTeamPage />} />
                <Route path="reports" element={<ManagerReportsPage />} />
                <Route path="settings" element={<ManagerSettingsPage />} />
                <Route index element={<Navigate to="dashboard" replace />} />
              </Route>

              <Route
                path="user/*"
                element={
                  <ProtectedRoute allowedRoles={[ROLES.WEBSITE_USER]}>
                    <DashboardLayout portalTitle="My Workspace" />
                  </ProtectedRoute>
                }
              >
                <Route path="dashboard" element={<UserOverview />} />
                <Route path="profile" element={<UserProfilePage />} />
                <Route path="preferences" element={<UserPreferencesPage />} />
                <Route path="activity" element={<UserActivityPage />} />
                <Route index element={<Navigate to="dashboard" replace />} />
              </Route>

              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </AuthModalProvider>
        </AuthProvider>
      </ThemeProvider>
    </BrowserRouter>
  );
}
