"use client";

import { useState, useTransition } from "react";
import { useRouter } from "next/navigation";

type Visibility = "private" | "unlisted" | "public";

export default function VisibilitySelect({
  fileId,
  initial,
}: {
  fileId: string;
  initial: Visibility;
}) {
  const [visibility, setVisibility] = useState<Visibility>(initial);
  const [error, setError] = useState<string | null>(null);
  const [pending, startTransition] = useTransition();
  const router = useRouter();

  const update = (next: Visibility) => {
    if (next === visibility) return;
    const previous = visibility;
    setVisibility(next);
    setError(null);

    startTransition(async () => {
      try {
        const res = await fetch(`/api/files/${fileId}/visibility`, {
          method: "PATCH",
          headers: { "content-type": "application/json" },
          body: JSON.stringify({ visibility: next }),
        });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        router.refresh();
      } catch (err) {
        setVisibility(previous);
        setError(err instanceof Error ? err.message : "Failed to update");
      }
    });
  };

  const options: { value: Visibility; label: string; hint: string }[] = [
    { value: "private", label: "Private", hint: "Only you can view" },
    { value: "unlisted", label: "Unlisted", hint: "Anyone with the link, hidden from your uploads" },
    { value: "public", label: "Public", hint: "Anyone can view, shown on your profile" },
  ];

  return (
    <div className="flex flex-col gap-2">
      <label className="text-mainwhite/70 text-sm">Visibility</label>
      <div className="flex gap-2 flex-wrap">
        {options.map((opt) => (
          <button
            key={opt.value}
            type="button"
            onClick={() => update(opt.value)}
            disabled={pending}
            title={opt.hint}
            className={
              "rounded-md border px-3 py-1.5 text-sm transition " +
              (visibility === opt.value
                ? "border-mainorange text-mainorange"
                : "border-maingrey text-mainwhite hover:border-mainorange hover:text-mainorange") +
              (pending ? " opacity-60 cursor-wait" : "")
            }
          >
            {opt.label}
          </button>
        ))}
      </div>
      <span className="text-mainwhite/60 text-xs">
        {options.find((o) => o.value === visibility)?.hint}
      </span>
      {error && <span className="text-red-400 text-xs">{error}</span>}
    </div>
  );
}
