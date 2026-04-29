"use client";
import Link from "next/link";
import { useEffect, useRef } from "react";
import { FaGithub } from "react-icons/fa";
export function HeroVideoSection() {
  const videoRef = useRef<HTMLVideoElement>(null);

  useEffect(() => {
    if (videoRef.current) {
      videoRef.current.playbackRate = 1.55;
    }
  }, []);

  return (
    <section className="relative h-[75vh] w-full overflow-hidden">
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

      <div className="relative z-10 flex h-full items-center justify-center">
        <div className="max-w-3xl text-center px-6">
          <h1 className="text-4xl md:text-5xl font-bold text-white">
            Upload and share files instantly
          </h1>

          <p className="mt-4 text-lg text-white/80">
            Upload, embed, and share files in seconds. No limits. No effort.
          </p>

          <div className="mt-8 flex justify-center gap-3">
            <Link
              href="/download"
              className="w-70 justify-center bg-mainorange/70 hover:bg-mainorange px-6 py-4 text-lg rounded-md flex items-center gap-3 font-medium text-mainwhite transition"
            >
              Download
            </Link>
            <Link
              href="https://github.com/smhussa22/filespark"
              className="w-70 justify-center bg-neutral-600/70 hover:bg-neutral-600 px-6 py-4 text-lg rounded-md flex items-center gap-3 font-medium text-mainwhite transition"
            >
              <FaGithub size={36} />
              View Source Code
            </Link>
          </div>
        </div>
      </div>
    </section>
  );
}
