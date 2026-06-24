export default function LoginBackground() {
  return (
    <div className="pointer-events-none fixed inset-0 overflow-hidden" aria-hidden="true">
      <div className="absolute inset-0 bg-[var(--bg-base)] transition-colors duration-300" />

      <div
        className="animate-aurora absolute -left-[20%] -top-[30%] h-[70%] w-[70%] rounded-full opacity-40 blur-[120px]"
        style={{ background: 'radial-gradient(circle, var(--aurora-1) 0%, transparent 70%)' }}
      />
      <div
        className="animate-aurora-delayed absolute -right-[15%] top-[10%] h-[60%] w-[60%] rounded-full opacity-35 blur-[100px]"
        style={{ background: 'radial-gradient(circle, var(--aurora-2) 0%, transparent 70%)' }}
      />
      <div
        className="animate-aurora absolute bottom-[-20%] left-[25%] h-[55%] w-[55%] rounded-full opacity-30 blur-[110px]"
        style={{ background: 'radial-gradient(circle, var(--aurora-3) 0%, transparent 70%)' }}
      />

      <div className="login-grid-bg absolute inset-0" />

      <div
        className="absolute inset-0 opacity-[0.015]"
        style={{
          backgroundImage: `url("data:image/svg+xml,%3Csvg viewBox='0 0 256 256' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.85' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)'/%3E%3C/svg%3E")`,
        }}
      />
    </div>
  );
}
