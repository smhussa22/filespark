"use client";

import Link from "next/link";
import { FaWindows, FaApple, FaUserCircle } from "react-icons/fa";
import { HiOutlineLogout } from "react-icons/hi";
import { useSession } from "../hooks/useSession";

export default function NavigationBar() {

  const { user, loading } = useSession();

  const os = "windows"; // @todo replace this with actual os checking
  const DownloadIcon = os === "windows" ? FaWindows : FaApple;

  return (

    <header className="w-full sticky top-0 z-50 bg-mainblack/70 backdrop-blur-md border-b border-maingrey/60">

      <nav className="mx-auto grid max-w-7xl grid-cols-2 items-center px-6 py-4">

        <div className="flex justify-start">

          <Link
            href="/"
            className="text-3xl font-semibold tracking-tight text-mainorange hover:text-mainorange/80 transition"
          >
            FileSpark
          </Link>

        </div>

        <div className="hidden md:flex justify-end items-center gap-3">

          <Link
            href="/download"
            className="rounded-md flex items-center gap-3 border border-maingrey w-36 justify-center px-4 py-2 font-medium text-mainwhite hover:border-mainorange hover:text-mainorange transition"
          >

            <DownloadIcon size={30} /> Download

          </Link>

          {!loading && user && (

            <Link
              href="/profile"
              aria-label="Profile"
              className="rounded-full border border-maingrey hover:border-mainorange transition overflow-hidden w-11 h-11 flex items-center justify-center"
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

            </Link>

          )}

          {!loading && !user && (

            <a
              href="/api/auth/google"
              className="rounded-md flex items-center gap-3 border border-maingrey w-36 justify-center px-4 py-2 font-medium text-mainwhite hover:border-mainorange hover:text-mainorange transition"
            >

              <HiOutlineLogout size={30} /> Sign In

            </a>

          )}

        </div>

      </nav>

    </header>

  );

}
