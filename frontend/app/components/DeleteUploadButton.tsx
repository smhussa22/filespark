"use client";

import { useState, useTransition } from "react";
import { createPortal } from "react-dom";
import { useRouter } from "next/navigation";
import { FaTrash } from "react-icons/fa";

export default function DeleteUploadButton({ fileId, name }: { fileId: string; name: string }) {
  const [pending, startTransition] = useTransition();
  const [error, setError] = useState<string | null>(null);
  const [open, setOpen] = useState(false);
  const router = useRouter();

  const openDialog = (event: React.MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();
    if (pending) return;
    setError(null);
    setOpen(true);
  };

  const closeDialog = () => {
    if (pending) return;
    setOpen(false);
  };

  const confirmDelete = () => {
    startTransition(async () => {
      setError(null);
      try {
        const res = await fetch(`/api/files/${encodeURIComponent(fileId)}`, { method: "DELETE" });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        setOpen(false);
        router.refresh();
      } catch (err) {
        setError(err instanceof Error ? err.message : "Failed to delete");
      }
    });
  };

  return (
    <>
      <button
        type="button"
        onClick={openDialog}
        disabled={pending}
        title="Delete"
        aria-label="Delete"
        className={
          "absolute top-1 right-1 rounded-md p-1.5 bg-mainblack/70 border border-maingrey text-mainwhite hover:text-red-400 hover:border-red-400 transition" +
          (pending ? " opacity-60 cursor-wait" : "")
        }
      >
        <FaTrash size={12} />
      </button>

      {open && typeof window !== "undefined" &&
        createPortal(
          <div
            className="fixed inset-0 z-[100] flex items-center justify-center bg-black/60 backdrop-blur-sm"
            role="dialog"
            aria-modal="true"
            onClick={closeDialog}
          >
            <div
              className="w-full max-w-md rounded-lg border border-maingrey bg-mainblack p-6 shadow-xl"
              onClick={(e) => e.stopPropagation()}
            >
              <h2 className="text-xl font-semibold text-mainwhite mb-2">Delete upload?</h2>
              <p className="text-mainwhite/70 mb-1">This will remove the file permanently and cannot be undone.</p>
              <p className="text-mainwhite truncate mb-4" title={name}>
                <span className="text-mainwhite/50">File: </span>
                {name}
              </p>

              {error && <p className="text-red-400 text-sm mb-4">{error}</p>}

              <div className="flex justify-end gap-2">
                <button
                  type="button"
                  onClick={closeDialog}
                  disabled={pending}
                  className="rounded-md border border-maingrey px-4 py-2 text-mainwhite hover:border-mainorange hover:text-mainorange transition"
                >
                  Cancel
                </button>
                <button
                  type="button"
                  onClick={confirmDelete}
                  disabled={pending}
                  className={
                    "rounded-md border border-red-500 bg-red-500/10 px-4 py-2 text-red-400 hover:bg-red-500/20 transition" +
                    (pending ? " opacity-60 cursor-wait" : "")
                  }
                >
                  {pending ? "Deleting..." : "Delete"}
                </button>
              </div>
            </div>
          </div>,
          document.body
        )}
    </>
  );
}
