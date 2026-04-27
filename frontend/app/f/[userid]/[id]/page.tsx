import type { Metadata } from "next";
import { cookies } from "next/headers";
import Link from "next/link";
import {
  FaFile,
  FaFilePdf,
  FaFilePowerpoint,
  FaFileWord,
  FaFileExcel,
  FaFileArchive,
  FaFileAudio,
  FaFileCode,
  FaFileAlt,
  FaDownload,
  FaExpand,
  FaEye,
  FaLock,
} from "react-icons/fa";
import VisibilitySelect from "../../../components/VisibilitySelect";

type Visibility = "private" | "unlisted" | "public";

type FileMeta = {
  id: string;
  ownerId: string;
  name: string;
  mime: string;
  sizeBytes: number;
  signedUrl: string;
  visibility: Visibility;
  viewCount: number;
  downloadCount: number;
  isOwner: boolean;
};

async function fetchMeta(userid: string, id: string): Promise<{ meta: FileMeta | null; status: number; body: string }> {
  const backend = process.env.BACKEND_URL;
  if (!backend) return { meta: null, status: 500, body: "BACKEND_URL is not set" };

  const cookieStore = await cookies();
  const token = cookieStore.get("ga_session")?.value;

  const headers: Record<string, string> = {};
  if (token) headers.Authorization = `Bearer ${token}`;

  const url = `${backend.replace(/\/$/, "")}/f/${encodeURIComponent(userid)}/${encodeURIComponent(id)}/meta`;

  let res: Response;
  try {
    res = await fetch(url, { headers, cache: "no-store" });
  } catch (err) {
    console.error(`[viewer] fetch ${url} threw:`, err);
    return { meta: null, status: 0, body: String(err) };
  }

  if (!res.ok) {
    const body = await res.text().catch(() => "");
    console.error(`[viewer] ${url} -> ${res.status} ${body}`);
    return { meta: null, status: res.status, body };
  }

  try {
    return { meta: (await res.json()) as FileMeta, status: 200, body: "" };
  } catch (err) {
    console.error(`[viewer] ${url} -> 200 but JSON parse failed:`, err);
    return { meta: null, status: 500, body: String(err) };
  }
}

function formatBytes(bytes: number): string {
  if (!Number.isFinite(bytes) || bytes <= 0) return "";
  if (bytes < 1024) return `${bytes} B`;
  const units = ["KB", "MB", "GB", "TB"];
  let value = bytes / 1024;
  let unit = 0;
  while (value >= 1024 && unit < units.length - 1) {
    value /= 1024;
    unit++;
  }
  return `${value.toFixed(value >= 10 ? 0 : 1)} ${units[unit]}`;
}

function iconFor(mime: string, name: string) {
  const ext = name.split(".").pop()?.toLowerCase() ?? "";
  const lower = mime.toLowerCase();
  if (lower === "application/pdf") return FaFilePdf;
  if (lower.includes("presentation") || ext === "ppt" || ext === "pptx") return FaFilePowerpoint;
  if (lower.includes("word") || ext === "doc" || ext === "docx") return FaFileWord;
  if (lower.includes("sheet") || lower.includes("excel") || ext === "xls" || ext === "xlsx" || ext === "csv") return FaFileExcel;
  if (lower.startsWith("audio/")) return FaFileAudio;
  if (
    lower === "application/zip" ||
    lower === "application/x-7z-compressed" ||
    lower === "application/x-rar-compressed" ||
    ["zip", "rar", "7z"].includes(ext)
  ) return FaFileArchive;
  if (lower.startsWith("text/") || lower.includes("json") || lower.includes("xml")) return FaFileAlt;
  if (["js", "ts", "tsx", "py", "java", "go", "rs"].includes(ext)) return FaFileCode;
  return FaFile;
}

function Viewer({ meta, rawUrl }: { meta: FileMeta; rawUrl: string }) {
  const lower = meta.mime.toLowerCase();

  if (lower.startsWith("image/")) {
    return (
      // eslint-disable-next-line @next/next/no-img-element
      <img src={rawUrl} alt={meta.name} className="max-w-full max-h-[70vh] object-contain rounded-md" />
    );
  }

  if (lower.startsWith("video/")) {
    return (
      <video src={rawUrl} controls playsInline className="max-w-full max-h-[70vh] rounded-md">
        Your browser does not support video playback.
      </video>
    );
  }

  if (lower.startsWith("audio/")) {
    return (
      <audio src={rawUrl} controls className="w-full">
        Your browser does not support audio playback.
      </audio>
    );
  }

  if (lower === "application/pdf") {
    return (
      <iframe src={rawUrl} className="w-full h-[70vh] rounded-md bg-mainblack" title={meta.name} />
    );
  }

  if (lower.startsWith("text/") || lower.includes("json") || lower.includes("xml")) {
    return (
      <iframe src={rawUrl} className="w-full h-[70vh] rounded-md bg-mainblack" title={meta.name} />
    );
  }

  const Icon = iconFor(meta.mime, meta.name);
  return (
    <div className="flex flex-col items-center justify-center gap-4 py-16">
      <Icon size={96} className="text-mainwhite/70" />
      <p className="text-mainwhite/70 text-sm">No inline preview available for this file type.</p>
    </div>
  );
}

export async function generateMetadata({
  params,
}: {
  params: Promise<{ userid: string; id: string }>;
}): Promise<Metadata> {

  const { userid, id } = await params;
  const { meta } = await fetchMeta(userid, id);

  const headerStore = await import("next/headers").then(m => m.headers()).catch(() => null);
  let origin = process.env.NEXT_PUBLIC_BASE_URL?.replace(/\/$/, "") ?? "";
  if (!origin && headerStore) {
    const h = await headerStore;
    const host = h.get("x-forwarded-host") ?? h.get("host");
    const proto = h.get("x-forwarded-proto") ?? "https";
    if (host) origin = `${proto}://${host}`;
  }
  if (!origin) origin = "https://getfilespark.tech";

  const pageUrl = `${origin}/f/${encodeURIComponent(userid)}/${encodeURIComponent(id)}`;

  if (!meta) {
    const fallbackTitle = "Shared file · FileSpark";
    const fallbackDescription = "View or download a file shared on FileSpark — fast, free, no size limits, no signup required to view.";
    return {
      metadataBase: new URL(origin),
      title: fallbackTitle,
      description: fallbackDescription,
      openGraph: {
        type: "website",
        title: fallbackTitle,
        description: fallbackDescription,
        siteName: "FileSpark",
        url: pageUrl,
      },
      twitter: {
        card: "summary",
        title: fallbackTitle,
        description: fallbackDescription,
      },
    };
  }

  const isViewable = meta.visibility !== "private";
  const lower = (meta.mime || "").toLowerCase();

  // Use the signed S3 URL directly as the embed asset so crawlers fetch bytes without
  // following any redirects.
  const assetUrl = isViewable ? meta.signedUrl : null;

  const title = meta.name || "Shared file";
  const sizeText = formatBytes(meta.sizeBytes);
  const typeText = meta.mime || "file";
  const description = `${title} — ${sizeText} ${typeText}. Shared via FileSpark, the free, no-signup-required file sharing service. Click to view or download.`;

  const og: NonNullable<Metadata["openGraph"]> = {
    type: "website",
    title: `${title} · FileSpark`,
    description,
    siteName: "FileSpark",
    url: pageUrl,
  };
  // Loosely typed because Next.js's twitter card is a discriminated union (summary vs
  // summary_large_image vs player) and we switch between them based on MIME.
  const twitter: Record<string, unknown> = {
    title: `${title} · FileSpark`,
    description,
    card: "summary",
  };

  if (assetUrl) {
    if (lower.startsWith("image/")) {
      og.images = [{ url: assetUrl, alt: title }];
      twitter.card = "summary_large_image";
      twitter.images = [assetUrl];
    } else if (lower.startsWith("video/")) {
      og.videos = [{
        url: assetUrl,
        secureUrl: assetUrl,
        type: meta.mime || "video/mp4",
      }];
      og.images = [{ url: assetUrl, alt: title }];
      twitter.card = "player";
      twitter.images = [assetUrl];
    } else if (lower.startsWith("audio/")) {
      og.audio = [{ url: assetUrl, type: meta.mime || "audio/mpeg" }];
    }
  }

  return {
    metadataBase: new URL(origin),
    title: `${title} · FileSpark`,
    description,
    openGraph: og,
    twitter: twitter as Metadata["twitter"],
  };

}

export default async function FileViewerPage({
  params,
}: {
  params: Promise<{ userid: string; id: string }>;
}) {
  const { userid, id } = await params;
  const { meta, status, body } = await fetchMeta(userid, id);

  if (!meta) {
    const message =
      status === 403
        ? "This file is private."
        : status === 404
        ? "File not found."
        : `Could not load file (HTTP ${status}).`;

    return (
      <main className="min-h-[calc(100vh-80px)] flex items-center justify-center px-6 py-12">
        <div className="flex flex-col items-center gap-4 text-center max-w-xl">
          {status === 403 ? (
            <FaLock size={56} className="text-mainwhite/70" />
          ) : (
            <FaFile size={56} className="text-mainwhite/70" />
          )}
          <h1 className="text-2xl font-semibold text-mainwhite">{message}</h1>
          {body && status !== 403 && status !== 404 && (
            <pre className="text-xs text-mainwhite/50 whitespace-pre-wrap break-words max-w-full">{body}</pre>
          )}
          <Link href="/" className="text-mainorange hover:underline">
            Back home
          </Link>
        </div>
      </main>
    );
  }

  const rawUrl = `/f/${encodeURIComponent(userid)}/${encodeURIComponent(id)}/raw`;
  const downloadUrl = `/f/${encodeURIComponent(userid)}/${encodeURIComponent(id)}/download`;

  return (
    <main className="min-h-[calc(100vh-80px)] flex justify-center px-6 py-8">
      <div className="w-full max-w-5xl flex flex-col gap-6">

        <header className="flex flex-wrap items-start justify-between gap-4">

          <div className="min-w-0">
            <h1 className="text-2xl font-semibold text-mainwhite truncate" title={meta.name}>
              {meta.name}
            </h1>
            <div className="text-mainwhite/60 text-sm flex gap-3 flex-wrap">
              <span>{meta.mime}</span>
              {meta.sizeBytes > 0 && <span>{formatBytes(meta.sizeBytes)}</span>}
              <span className="flex items-center gap-1">
                <FaEye size={12} /> {meta.viewCount} views
              </span>
              <span className="flex items-center gap-1">
                <FaDownload size={12} /> {meta.downloadCount} downloads
              </span>
            </div>
          </div>

          <div className="flex gap-2">
            <a
              href={rawUrl}
              target="_blank"
              rel="noreferrer"
              className="rounded-md border border-maingrey px-3 py-2 text-mainwhite hover:border-mainorange hover:text-mainorange transition flex items-center gap-2"
            >
              <FaExpand /> Full screen
            </a>
            <a
              href={downloadUrl}
              className="rounded-md border border-maingrey px-3 py-2 text-mainwhite hover:border-mainorange hover:text-mainorange transition flex items-center gap-2"
            >
              <FaDownload /> Download
            </a>
          </div>

        </header>

        <section className="rounded-lg border border-maingrey bg-mainblack/60 p-4 flex items-center justify-center">
          <Viewer meta={meta} rawUrl={rawUrl} />
        </section>

        {meta.isOwner && (
          <section className="rounded-lg border border-maingrey bg-mainblack/60 p-4">
            <VisibilitySelect fileId={meta.id} initial={meta.visibility} />
          </section>
        )}

      </div>
    </main>
  );
}
