import { Link } from 'react-router-dom';
import PageHeader from '../../../components/dashboard/PageHeader';

const ROLES = [
  {
    id: 'SUPER_ADMIN',
    name: 'Super Administrator',
    description: 'Highest privilege — full access to all user types including other super admins, admins, managers, and website users.',
    permissions: ['Create super admins', 'Create admins', 'Manage managers', 'Manage website users', 'View analytics', 'System settings'],
    managePath: '/admin/super-admins',
  },
  {
    id: 'ADMIN',
    name: 'Administrator',
    description: 'Platform administrator — manage admins, managers, and website users.',
    permissions: ['Create admins', 'Manage managers', 'Manage website users', 'View analytics', 'System settings'],
    managePath: '/admin/admins',
  },
  {
    id: 'MANAGER',
    name: 'Manager',
    description: 'Regional oversight — manage website users and team assignments in assigned regions.',
    permissions: ['Manage website users', 'View team reports', 'Regional settings'],
    managePath: '/admin/managers',
  },
  {
    id: 'USER',
    name: 'Website user',
    description: 'Public account — access workspace, profile, preferences, and AI tools.',
    permissions: ['Own profile', 'Preferences', 'AI chat', 'Activity history'],
    managePath: '/admin/users',
  },
];

export default function AdminRolesPage() {
  return (
    <div className="dashboard-page space-y-6">
      <PageHeader description="Role definitions and permission scopes for every account type on the platform." />

      <div className="grid gap-4 lg:grid-cols-3">
        {ROLES.map((role) => (
          <article key={role.id} className="dashboard-role-card">
            <div className="dashboard-role-card-head">
              <span className="dashboard-role-pill">{role.id}</span>
              <h3 className="text-lg font-semibold text-[#1a1a1a]">{role.name}</h3>
            </div>
            <p className="mt-2 text-sm text-[#555555]">{role.description}</p>
            <ul className="mt-4 space-y-1.5">
              {role.permissions.map((perm) => (
                <li key={perm} className="flex items-center gap-2 text-sm text-[#333333]">
                  <span className="text-[#c05a2b]" aria-hidden="true">✓</span>
                  {perm}
                </li>
              ))}
            </ul>
            <Link to={role.managePath} className="btn-gem-outline mt-5 inline-flex rounded px-3 py-2 text-xs font-semibold">
              Manage {role.name.toLowerCase()}s
            </Link>
          </article>
        ))}
      </div>
    </div>
  );
}
