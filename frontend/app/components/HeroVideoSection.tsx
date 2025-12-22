"use client";
import Link from "next/link";
import { FaWindows, FaApple } from "react-icons/fa";
export function HeroVideoSection() {
  const os = "windows";
  const DownloadIcon = os === "windows" ? FaWindows : FaApple;

  return (
    <section className="relative h-[75vh] w-full overflow-hidden">
      <video
        className="absolute inset-0 h-full w-full object-cover"
        autoPlay
        muted
        loop
        playsInline
        preload="auto"
      >
        <source
          src="https://cdn.medal.tv/assets/next/video/home_hero_new.0fc056a5.webm"
          type="video/webm"
        />
        <source
          src="https://cdn.medal.tv/assets/next/video/home_hero_new.5bb3dede.mp4"
          type="video/mp4"
        />
      </video>

      <div className="absolute inset-0 bg-black/60" />

      <div className="relative z-10 flex h-full items-center justify-center">
        <div className="max-w-3xl text-center px-6">
          <h1 className="text-4xl md:text-5xl font-bold text-white">
            Upload and share files instantly
          </h1>

          <p className="mt-4 text-lg text-white/80">
            Upload, embed, and share files in seconds. No limits. No effort.
          </p>

          <div className="mt-8 flex justify-center">
            <Link
              href="/download"
              className="bg-mainorange/70 hover:bg-mainorange px-6 py-4 text-lg rounded-md flex items-center gap-3 font-medium text-mainwhite transition"
            >
              <DownloadIcon size={36} />
              Download FileSpark
            </Link>
          </div>
        </div>
      </div>
    </section>
  );
}
