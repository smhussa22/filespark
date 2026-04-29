"use client";

import Link from "next/link";
import { HiOutlineLogout } from "react-icons/hi";
import { useSession } from "../hooks/useSession";
import AccountMenu from "./AccountMenu";

export default function NavigationBar() {

  const { user, loading } = useSession();

  return (

    <header className="w-full sticky top-0 z-50 bg-mainblack/70 backdrop-blur-md border-b border-maingrey/60">

      <nav className="mx-auto grid max-w-7xl grid-cols-2 items-center px-4 sm:px-6 py-3 sm:py-4">

        <div className="flex justify-start">

          <Link
            href="/"
            className="text-2xl sm:text-3xl font-semibold tracking-tight text-mainorange hover:text-mainorange/80 transition"
          >
            FileSpark
          </Link>

        </div>

        <div className="flex justify-end items-center gap-2 sm:gap-3">

          <Link
            href="/download"
            className="rounded-md flex items-center gap-2 sm:gap-3 border border-maingrey justify-center px-3 sm:px-4 py-2 text-sm sm:text-base sm:w-36 font-medium text-mainwhite hover:border-mainorange hover:text-mainorange transition"
          >

            Download

          </Link>

          {!loading && user && <AccountMenu user={user} />}

          {!loading && !user && (

            <a
              href="/api/auth/google"
              className="hidden md:flex rounded-md items-center gap-3 border border-maingrey w-36 justify-center px-4 py-2 font-medium text-mainwhite hover:border-mainorange hover:text-mainorange transition"
            >

              <HiOutlineLogout size={30} /> Sign In

            </a>

          )}

        </div>

      </nav>

    </header>

  );

}
