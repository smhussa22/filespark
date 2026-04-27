import type { NextConfig } from "next";

const backend = process.env.BACKEND_URL?.replace(/\/$/, "") ?? "http://localhost:8000";

const nextConfig: NextConfig = {
  images: {
    remotePatterns: [
      {
        protocol: "https",
        hostname: "lh3.googleusercontent.com",
      },
      {
        protocol: "https",
        hostname: "profiles.google.com",
      },
    ],
  },
  async rewrites() {
    return [
      { source: "/f/:userid/:id/raw", destination: `${backend}/f/:userid/:id/raw` },
      { source: "/f/:userid/:id/download", destination: `${backend}/f/:userid/:id/download` },
      { source: "/f/:userid/:id/meta", destination: `${backend}/f/:userid/:id/meta` },
    ];
  },
};

export default nextConfig;
