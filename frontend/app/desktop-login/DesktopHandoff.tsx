"use client";

import Link from "next/link";
import { useEffect, useRef, useState } from "react";

type Status =
  | { kind: "pending"; message: string }
  | { kind: "success"; message: string }
  | { kind: "error"; message: string }
  | { kind: "needs_auth" };

export default function DesktopHandoff({ port }: { port: string }) {
  const [status, setStatus] = useState<Status>({ kind: "pending", message: "Connecting to FileSpark Desktop..." });
  const sentRef = useRef(false);

  useEffect(() => {
    if (sentRef.current) return;
    sentRef.current = true;

    (async () => {
      try {
        const handoffRes = await fetch("/api/desktop-handoff", { method: "GET", cache: "no-store" });
        if (handoffRes.status === 401) {
          setStatus({ kind: "needs_auth" });
          return;
        }
        if (!handoffRes.ok) {
          throw new Error(`Could not load session (HTTP ${handoffRes.status})`);
        }
        const payload = await handoffRes.json();

        const localRes = await fetch(`http://127.0.0.1:${port}/callback`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        });
        if (!localRes.ok) {
          throw new Error(`Desktop client did not accept handoff (HTTP ${localRes.status})`);
        }

        setStatus({ kind: "success", message: "Logged in! You can return to FileSpark." });
      } catch (err) {
        setStatus({
          kind: "error",
          message:
            err instanceof Error
              ? `${err.message}. Make sure the FileSpark Desktop app is still open.`
              : "Could not reach the FileSpark Desktop app.",
        });
      }
    })();
  }, [port]);

  if (status.kind === "needs_auth") {
    const next = encodeURIComponent(`/desktop-login?port=${port}`);
    return (
      <main className="min-h-[calc(100vh-80px)] flex items-center justify-center px-6 py-12">
        <div className="max-w-md text-center">
          <h1 className="text-2xl font-semibold text-mainwhite mb-3">FileSpark Desktop</h1>
          <p className="text-mainwhite/80 mb-6">
            Sign in to FileSpark to finish connecting the desktop app.
          </p>
          <Link
            href={`/api/auth/google?next=${next}`}
            className="inline-flex items-center justify-center bg-mainorange/70 hover:bg-mainorange px-6 py-3 rounded-md font-medium text-mainwhite transition"
          >
            Continue with Google
          </Link>
          <p className="mt-6 text-sm text-mainwhite/50">
            After signing in you&apos;ll be returned here automatically.
          </p>
        </div>
      </main>
    );
  }

  const colour =
    status.kind === "success"
      ? "text-mainorange"
      : status.kind === "error"
      ? "text-red-400"
      : "text-mainwhite";

  return (
    <main className="min-h-[calc(100vh-80px)] flex items-center justify-center px-6 py-12">
      <div className="max-w-md text-center">
        <h1 className="text-2xl font-semibold text-mainwhite mb-3">FileSpark Desktop</h1>
        <p className={`${colour} mb-6`}>{status.message}</p>
        {status.kind === "error" && (
          <p className="text-mainwhite/60 text-sm">
            You can close this tab and try again from the desktop app, or{" "}
            <Link href="/" className="text-mainorange hover:underline">
              return to the homepage
            </Link>
            .
          </p>
        )}
        {status.kind === "success" && (
          <p className="text-mainwhite/60 text-sm">You can close this tab.</p>
        )}
      </div>
    </main>
  );
}
