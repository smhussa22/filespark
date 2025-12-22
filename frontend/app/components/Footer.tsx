"use client";

import Link from "next/link";

export default function Footer() {
  const navLinks = [
    { name: "Home", href: "/" },
    { name: "Downloads", href: "/download" },
    { name: "Changelog", href: "/changelog" },
    { name: "Account", href: "/account" },
  ];

  return (
    <footer className="bg-mainblack border-t border-maingrey">
      <div className="mx-auto max-w-7xl px-6 py-12">

        {/* Top row */}
        <div className="flex flex-col md:flex-row items-start md:items-center justify-between gap-8">

          {/* Logo + tagline */}
          <div>
            <Link
              href="/"
              className="text-2xl font-semibold tracking-tight text-mainorange hover:text-mainorange/80 transition"
            >
              FileSpark
            </Link>

            <p className="mt-3 max-w-sm text-sm text-mainwhite/60">
              Upload, embed, and share files in seconds. No limits. No effort.
            </p>
          </div>

          {/* Navigation */}
          <nav className="flex flex-wrap gap-x-8 gap-y-3">
            {navLinks.map((link) => (
              <Link
                key={link.name}
                href={link.href}
                className="text-sm font-medium text-mainwhite/70 hover:text-mainwhite transition"
              >
                {link.name}
              </Link>
            ))}
          </nav>

        </div>

      </div>
    </footer>
  );
}
