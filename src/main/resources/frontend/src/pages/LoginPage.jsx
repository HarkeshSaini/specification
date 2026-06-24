import { useEffect } from 'react';
import { Navigate, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Login from '../components/Login';
import LoginBackground from '../components/LoginBackground';
import ThemeToggle from '../components/theme/ThemeToggle';
import { getPostLoginPath } from '../utils/roles';

const FEATURES = [
  { title: 'Secure access', description: 'Enterprise-grade authentication with encrypted sessions.' },
  { title: 'Smart specifications', description: 'Manage and collaborate on project specs in one place.' },
  { title: 'Real-time sync', description: 'Stay aligned with your team across every update.' },
];

export default function LoginPage() {
  const navigate = useNavigate();
  const { isAuthenticated, role, redirectPath, login } = useAuth();

  useEffect(() => {
    if (isAuthenticated) {
      navigate(getPostLoginPath(role, redirectPath), { replace: true });
    }
  }, [isAuthenticated, role, redirectPath, navigate]);

  if (isAuthenticated) {
    return <Navigate to={getPostLoginPath(role, redirectPath)} replace />;
  }

  function handleSuccess(data) {
    const session = login(data);
    navigate(getPostLoginPath(session.role, session.redirectPath), { replace: true });
  }

  return (
    <div className="relative min-h-dvh">
      <LoginBackground />
      <div className="absolute right-4 top-4 z-20 sm:right-6 sm:top-6">
        <ThemeToggle />
      </div>
      <div className="relative z-10 flex min-h-dvh flex-col lg:flex-row">
        <section className="flex flex-1 flex-col justify-between px-4 py-8 sm:px-8 lg:px-16 lg:py-14">
          <div className="animate-fade-up">
            <p className="text-lg font-bold text-white">Specification</p>
            <p className="text-xs uppercase tracking-widest text-indigo-400/80">Platform</p>
          </div>
          <div className="my-6 max-w-lg lg:my-10">
            <h1 className="text-2xl font-bold text-white sm:text-3xl lg:text-4xl xl:text-5xl">
              Build better.
              <span className="block bg-gradient-to-r from-indigo-400 via-violet-400 to-cyan-400 bg-clip-text text-transparent">
                Ship faster.
              </span>
            </h1>
            <ul className="mt-6 space-y-3 lg:mt-10 lg:space-y-4">
              {FEATURES.map((f) => (
                <li key={f.title} className="text-sm text-slate-400">
                  <span className="font-semibold text-white">{f.title}</span> — {f.description}
                </li>
              ))}
            </ul>
          </div>
        </section>

        <section className="flex flex-1 items-center justify-center px-4 py-8 sm:px-8 lg:max-w-xl lg:px-12">
          <div className="glass-card w-full max-w-md rounded-2xl p-6 sm:rounded-3xl sm:p-10">
            <div className="mb-8">
              <h2 className="text-2xl font-bold text-white">Welcome back</h2>
              <p className="mt-2 text-sm text-slate-400">Sign in to continue to your workspace.</p>
            </div>
            <Login idPrefix="page" onSuccess={handleSuccess} />
          </div>
        </section>
      </div>
    </div>
  );
}
