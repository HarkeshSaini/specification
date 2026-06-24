import { Outlet } from 'react-router-dom';
import LoginBackground from '../LoginBackground';
import LoginModal from '../auth/LoginModal';
import Footer from './Footer';
import Header from './Header';

export default function MainLayout() {
  return (
    <div className="relative min-h-dvh">
      <LoginBackground />
      <div className="relative z-10 flex min-h-dvh flex-col">
        <Header />
        <main className="site-main flex-1">
          <Outlet />
        </main>
        <Footer />
      </div>
      <LoginModal />
    </div>
  );
}
