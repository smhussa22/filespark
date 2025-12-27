"use client";
import React from "react";
import { MdOutlineFileDownload } from "react-icons/md";
import { FaRegShareSquare } from "react-icons/fa";

export default function VideoPlayer({ src }: { src: string }) {

  return (

    <div className="bg-mainblack w-full max-w-5xl border border-white/10 rounded-md overflow-hidden flex flex-col">
      <video src={src} controls className="w-full bg-black" />

      <div className="px-5 py-4 flex items-start justify-between gap-6">
        <div className="flex flex-col gap-3 min-w-0">
          <h1 className="text-white text-lg font-bold truncate">
            fileName.extension
          </h1>

          <div className="flex items-center gap-3 min-w-0">
            <img
              src="/icon.svg"
              alt="User avatar"
              className="w-9 h-9 rounded-full bg-white/10 p-1"
            />

            <div className="flex flex-col leading-tight min-w-0">
              <span className="text-white text-sm font-medium truncate">
                FirstName
              </span>
              <span className="text-white/50 text-xs truncate">
                username@example.com
              </span>
            </div>
          </div>
        </div>

        <div className="flex flex-col items-end gap-3">
          <div className="text-right text-xs text-white/50 leading-tight">
            <span className="block">Dec 2, 2024</span>
            <span className="block">9:18 PM</span>
          </div>

          <div className="flex items-center gap-2">
            <button
              className="
                flex items-center justify-center gap-1
                w-28 h-9 text-sm
                bg-white/5 hover:bg-white/10
                border border-white/10 rounded
                text-white transition
              "
            >
              <MdOutlineFileDownload className="text-lg" />
              Download
            </button>

            <button
              className="
                flex items-center justify-center gap-1
                w-28 h-9 text-sm
                bg-white/5 hover:bg-white/10
                border border-white/10 rounded
                text-white transition
              "
            >
              <FaRegShareSquare className="text-lg" />
              Share
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
