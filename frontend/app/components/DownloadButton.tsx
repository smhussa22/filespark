import Link from "next/link";
import React from "react";
import { FaWindows } from "react-icons/fa";

export default function DownloadButton() {
  return (
    <>
      <Link
        href="/download"
        className="mx-4 hover:brightness-75 transition duration-200 rounded-md bg-mainorange px-4 py-2 flex gap-2 justify-center items-center font-semibold"
      >
        <FaWindows size={30} />
        <h1>Download</h1>
      </Link>
    </>
  );
}
