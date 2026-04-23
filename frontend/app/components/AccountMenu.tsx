"use client";

import { useEffect, useRef, useState } from "react";
import Link from "next/link";
import { FaUserCircle } from "react-icons/fa";
import type { SessionUser } from "../hooks/useSession";

export default function AccountMenu({ user }: { user: NonNullable<SessionUser> }) {
  const [open, setOpen] = useState(false);
  const containerRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    if (!open) return;
    const onClick = (event: MouseEvent) => {
      if (!containerRef.current) return;
      if (!containerRef.current.contains(event.target as Node)) setOpen(false);
    };
    const onKey = (event: KeyboardEvent) => {
      if (event.key === "Escape") setOpen(false);
    };
    window.addEventListener("mousedown", onClick);
    window.addEventListener("keydown", onKey);
    return () => {
      window.removeEventListener("mousedown", onClick);
      window.removeEventListener("keydown", onKey);
    };
  }, [open]);

  return (
    <div className="relative" ref={containerRef}>
      <button
        type="button"
        onClick={() => setOpen((v) => !v)}
        aria-haspopup="menu"
        aria-expanded={open}
        aria-label="Account menu"
        className="rounded-full border border-maingrey hover:border-mainorange transition overflow-hidden w-11 h-11 flex items-center justify-center bg-mainblack"
      >
        {user.picture ? (
          // eslint-disable-next-line @next/next/no-img-element
          <img
            src={user.picture}
            alt={user.name ?? "Account"}
            width={44}
            height={44}
            referrerPolicy="no-referrer"
            className="w-full h-full object-cover"
          />
        ) : (
          <FaUserCircle size={36} className="text-mainwhite" />
        )}
      </button>

      {open && (
        <div
          role="menu"
          className="absolute right-0 mt-2 w-48 rounded-md border border-maingrey bg-mainblack shadow-lg z-50 overflow-hidden"
        >
          <div className="px-3 py-2 border-b border-maingrey">
            <div className="text-mainwhite text-sm truncate">{user.name ?? "FileSpark User"}</div>
            {user.email && <div className="text-mainwhite/60 text-xs truncate">{user.email}</div>}
          </div>

          <Link
            href="/profile"
            role="menuitem"
            onClick={() => setOpen(false)}
            className="block px-3 py-2 text-mainwhite hover:bg-maingrey transition"
          >
            View profile
          </Link>

          <form action="/api/auth/logout" method="post">
            <button
              type="submit"
              role="menuitem"
              className="w-full text-left px-3 py-2 text-mainwhite hover:bg-maingrey transition"
            >
              Log out
            </button>
          </form>
        </div>
      )}
    </div>
  );
}
