import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { useAuthModal } from '../../context/AuthModalContext';
import { getPostLoginPath } from '../../utils/roles';
import Login from '../Login';
import Modal from '../ui/Modal';
import CreateAccountForm from './CreateAccountForm';
import ForgotPasswordForm from './ForgotPasswordForm';

const TABS = [
  { id: 'login', label: 'Sign in' },
  { id: 'register', label: 'Create account' },
  { id: 'forgot', label: 'Forgot password' },
];

export default function LoginModal() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const { open, activeTab, setActiveTab, closeModal } = useAuthModal();

  function handleLoginSuccess(data) {
    const session = login(data);
    closeModal();
    navigate(getPostLoginPath(session.role, session.redirectPath));
  }

  const titles = {
    login: 'Welcome back',
    register: 'Create your account',
    forgot: 'Reset password',
  };

  return (
    <Modal open={open} onClose={closeModal} title={titles[activeTab]} size="lg">
      <div className="mb-6 flex flex-col gap-1 rounded-lg bg-[#f4f4f4] p-1 sm:flex-row">
        {TABS.map((tab) => (
          <button
            key={tab.id}
            type="button"
            onClick={() => setActiveTab(tab.id)}
            className={`flex-1 rounded-md px-2 py-2.5 text-xs font-semibold transition-all sm:px-3 sm:text-sm ${
              activeTab === tab.id
                ? 'bg-white text-[#c05a2b] shadow-sm ring-1 ring-[#e0e0e0]'
                : 'text-[#555555] hover:text-[#1a1a1a]'
            }`}
          >
            {tab.label}
          </button>
        ))}
      </div>

      {activeTab === 'login' ? (
        <>
          <p className="mb-5 text-sm text-[#555555]">Enter your credentials to access your workspace.</p>
          <Login idPrefix="modal" onSuccess={handleLoginSuccess} onForgot={() => setActiveTab('forgot')} />
        </>
      ) : null}

      {activeTab === 'register' ? (
        <CreateAccountForm onSwitchLogin={() => setActiveTab('login')} />
      ) : null}

      {activeTab === 'forgot' ? (
        <ForgotPasswordForm onSwitchLogin={() => setActiveTab('login')} />
      ) : null}
    </Modal>
  );
}
