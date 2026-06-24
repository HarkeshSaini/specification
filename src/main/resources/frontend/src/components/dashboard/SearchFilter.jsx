import { useState } from 'react';
import { DashboardInput } from './DashboardField';
import DashboardIcon from './DashboardIcon';

export default function SearchFilter({ placeholder = 'Search…', onSearch, initialValue = '' }) {
  const [query, setQuery] = useState(initialValue);

  function handleSubmit(e) {
    e.preventDefault();
    onSearch?.(query.trim());
  }

  return (
    <form onSubmit={handleSubmit} className="dashboard-search">
      <DashboardIcon name="search" className="dashboard-search-icon" />
      <DashboardInput
        type="search"
        value={query}
        onChange={(e) => {
          setQuery(e.target.value);
          if (!e.target.value) onSearch?.('');
        }}
        placeholder={placeholder}
        className="dashboard-search-input"
        aria-label={placeholder}
      />
    </form>
  );
}
