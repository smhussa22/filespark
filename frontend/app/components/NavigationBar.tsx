"use client";
import Link from "next/link";
import Image from "next/image";
import { useState } from "react";
import { VscMenu } from "react-icons/vsc";
import DownloadButton from "./DownloadButton";

export default function NavigationBar() {

  const [open, setOpen] = useState(false);

  const navigationLinks = [

    { name: "Home", href: "/" },
    { name: "Download", href: "/download" },
    { name: "Changelog", href: "/changelog" },

  ];

  return (

    <header className="w-full bg-mainblack text-mainwhite shadow-xl">

      <nav className="flex items-center justify-between md:justify-center px-4">

        <Link draggable={false} href="/" className="flex select-none items-center gap-4 p-4 hover:brightness-75 transition duration-200">

          <Image draggable={false} src="icon.svg" alt="FileSpark Logo" width={50} height={50} />
          <h1 className="text-2xl tracking-tight font-medium">FileSpark</h1>

        </Link>

        <div className="hidden md:flex items-center">

          {navigationLinks.map(link => (

            <Link draggable="false" key={link.href} href={link.href} className="select-none p-4 hover:brightness-75 transition duration-200 text-xl tracking-tight font-medium">

              {link.name}

            </Link>

          ))}

          <Link draggable="false" href="/login" className="text-mainorange select-none p-4 hover:brightness-75 transition duration-200 text-xl tracking-tight font-medium">Login</Link>
          <DownloadButton />

        </div>

        <button onClick={() => setOpen(!open)} className="md:hidden p-4 hover:brightness-75 transition duration-200" aria-label="Open menu">

          <VscMenu size={28} />

        </button>

      </nav>

      <div className={`md:hidden overflow-hidden transition-all duration-300 ease-in-out ${open ? "max-h-96 opacity-100" : "max-h-0 opacity-0"}`}>

        <div className="flex flex-col px-4 pb-4">

          {navigationLinks.map(link => (

            <Link key={link.href} href={link.href} className="py-3 text-xl tracking-tight font-medium hover:bg-white/10 rounded-md" onClick={() => setOpen(false)}>

              {link.name}

            </Link>

          ))}

          <Link href="/login" className="py-3 text-xl tracking-tight font-medium text-mainorange" onClick={() => setOpen(false)}>Login</Link>

        </div>

      </div>

    </header>

  );

}
