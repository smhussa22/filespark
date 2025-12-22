"use client";

import Link from "next/link";
import { FaWindows, FaApple } from "react-icons/fa";
import { useSession } from "../hooks/useSession";
import { HiOutlineLogout } from "react-icons/hi";

export default function NavigationBar() {

  const { user, loading } = useSession();

  const os = "windows"; // @todo replace this with actual os checking
  const DownloadIcon = os === "windows" ? FaWindows: FaApple;

  const navLinks = [

    { name: "Home", href: "/" },
    { name: "Downloads", href: "/download" },
    { name: "Changelog", href: "/changelog" },
    { name: "Account", href: "/account" },

  ];

  return (

    <header className="w-full sticky top-0 z-50 bg-mainblack/70 backdrop-blur-md border-b border-maingrey/60">

      <nav className="mx-auto grid max-w-7xl grid-cols-3 items-center px-6 py-4">

        <div className="flex justify-start">

          <Link
            href="/"
            className="text-3xl font-semibold tracking-tight text-mainorange hover:text-mainorange/80 transition"
          >
            FileSpark
          </Link>

        </div>

        <div className="hidden md:flex justify-center gap-8">

          {navLinks.map((link) => (

            <Link
              key={link.name}
              href={link.href}
              className="font-medium text-mainwhite hover:text-mainwhite/80 transition"
            >

              {link.name}

            </Link>

          ))}

        </div>

        <div className="hidden md:flex justify-end items-center gap-3">

          <Link
            href="/download"
            className="rounded-md flex items-center gap-3 border border-maingrey w-36 justify-center px-4 py-2 font-medium text-mainwhite hover:border-mainorange hover:text-mainorange transition"
          >

            <DownloadIcon size={30}/> Download

          </Link>

          <Link
            href="/signin"
            className="rounded-md flex items-center gap-3 border border-maingrey w-36 justify-center px-4 py-2 font-medium text-mainwhite hover:border-mainorange hover:text-mainorange transition"
          >

            <HiOutlineLogout size={30} /> Sign In

          </Link>
       
        </div>

      </nav>

    </header>

  );

}
