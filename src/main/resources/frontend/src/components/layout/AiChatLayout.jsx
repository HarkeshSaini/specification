import { Outlet } from 'react-router-dom';
import LoginModal from '../auth/LoginModal';
import Header from './Header';

export default function AiChatLayout() {
  return (
    <div className="ai-chat-layout">
      <Header />
      <Outlet />
      <LoginModal />
    </div>
  );
}
