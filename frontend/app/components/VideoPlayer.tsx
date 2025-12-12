"use client";
import React from "react";

export default function ({ src }: { src: string }) {
  return (
    <>
      <video
        src={src}
        controls
        className="w-full max-w-4xl rounded-xl shadow-lg"
      />
    </>
  );
}
