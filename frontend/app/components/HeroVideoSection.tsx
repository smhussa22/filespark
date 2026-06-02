"use client";
import Link from "next/link";
import { useEffect, useRef } from "react";
import { FaGithub } from "react-icons/fa";

type HeroStats = { totalUsers: number; totalUploads: number };

export function HeroVideoSection({ stats }: { stats: HeroStats }) {
  const videoRef = useRef<HTMLVideoElement>(null);

  useEffect(() => {
    if (videoRef.current) {
      videoRef.current.playbackRate = 1.55;
    }
  }, []);

  return (
    <section className="relative min-h-[70vh] sm:min-h-[75vh] md:min-h-[80vh] w-full overflow-hidden">
      <video
        ref={videoRef}
        className="absolute inset-0 h-full w-full object-cover"
        autoPlay
        muted
        loop
        playsInline
        preload="auto"
      >
        <source src="/demo-of-filespark.mp4" type="video/mp4" />
      </video>

      <div className="absolute inset-0 bg-black/60" />

      <div className="relative z-10 flex min-h-[70vh] sm:min-h-[75vh] md:min-h-[80vh] items-center justify-center py-10">
        <div className="max-w-3xl text-center px-4 sm:px-6">
          <h1 className="text-3xl sm:text-4xl md:text-5xl font-bold text-white">
            Upload and share files instantly
          </h1>

          <p className="mt-3 sm:mt-4 text-base sm:text-lg text-white/80">
            Upload, embed, and share files in seconds. No limits. No effort.
          </p>

          <div className="mt-6 sm:mt-8 grid grid-cols-3 gap-3 sm:gap-6 max-w-2xl mx-auto">
            { /* <HeroStat value={stats.totalUploads.toLocaleString()} label="files uploaded" />
            <HeroStat value={stats.totalUsers.toLocaleString()} label="users connected" /> */}
            <HeroStat value="7,339" label="files uploaded" />
            <HeroStat value="112" label="users connected" />
            <HeroStat value="12+" label="supported file types" />
          </div>

          <div className="mt-6 sm:mt-8 flex flex-col sm:flex-row justify-center gap-3">
            <Link
              href="/download"
              className="w-full sm:w-70 justify-center bg-mainorange/70 hover:bg-mainorange px-6 py-3 sm:py-4 text-base sm:text-lg rounded-md flex items-center gap-3 font-medium text-mainwhite transition"
            >
              Download
            </Link>
            <Link
              href="https://github.com/smhussa22/filespark"
              className="w-full sm:w-70 justify-center bg-neutral-600/70 hover:bg-neutral-600 px-6 py-3 sm:py-4 text-base sm:text-lg rounded-md flex items-center gap-3 font-medium text-mainwhite transition"
            >
              <FaGithub size={28} className="sm:hidden" />
              <FaGithub size={36} className="hidden sm:block" />
              View Source Code
            </Link>
          </div>
        </div>
      </div>
    </section>
  );
}

function HeroStat({ value, label }: { value: string; label: string }) {
  return (
    <div className="rounded-lg border border-white/15 bg-black/40 backdrop-blur-sm px-2 sm:px-4 py-3 sm:py-4">
      <div className="text-xl sm:text-3xl font-bold text-white leading-tight">{value}</div>
      <div className="mt-1 text-[10px] sm:text-sm text-mainorange leading-tight">{label}</div>
    </div>
  );
}
