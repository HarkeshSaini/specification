import Modal from '../ui/Modal';

export default function DeleteConfirmModal({ open, onClose, onConfirm, title, message, loading }) {
  return (
    <Modal open={open} onClose={onClose} title={title ?? 'Confirm deletion'} size="md">
      <p className="text-sm text-slate-400">{message ?? 'This action will deactivate the account. Are you sure?'}</p>
      <div className="mt-6 flex justify-end gap-3">
        <button type="button" onClick={onClose} className="dashboard-btn-ghost rounded-xl px-4 py-2.5 text-sm">
          Cancel
        </button>
        <button
          type="button"
          disabled={loading}
          onClick={onConfirm}
          className="rounded-xl bg-red-500/90 px-4 py-2.5 text-sm font-semibold text-white transition-colors hover:bg-red-500 disabled:opacity-60"
        >
          {loading ? 'Deleting…' : 'Delete'}
        </button>
      </div>
    </Modal>
  );
}
