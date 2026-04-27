import { cookies } from "next/headers";
import { redirect } from "next/navigation";
import {
  FaUserCircle,
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
} from "react-icons/fa";
import DeleteUploadButton from "../components/DeleteUploadButton";

type UploadedFile = {
  id: string;
  ownerId?: string;
  name: string;
  mime: string;
  sizeBytes: number;
  createdAt: string;
  viewUrl: string;
};

function viewPath(file: UploadedFile, ownerId: string): string {
  return `/f/${encodeURIComponent(ownerId)}/${encodeURIComponent(file.id)}`;
}

type BackendUser = {
  id?: string;
  name?: string;
  email?: string;
  picture?: string;
};

async function backendFetch(path: string, token: string) {
  const backendUrl = process.env.BACKEND_URL;
  if (!backendUrl) throw new Error("BACKEND_URL is not set");
  return fetch(`${backendUrl.replace(/\/$/, "")}${path}`, {
    headers: { Authorization: `Bearer ${token}` },
    cache: "no-store",
  });
}

async function getSessionToken(): Promise<string | null> {
  const cookieStore = await cookies();
  return cookieStore.get("ga_session")?.value ?? null;
}

async function getBackendUser(token: string): Promise<BackendUser | null> {
  try {
    const res = await backendFetch("/users/me", token);
    if (!res.ok) {
      console.error(`[profile] /users/me failed: ${res.status}`);
      return null;
    }
    return (await res.json()) as BackendUser;
  } catch (err) {
    console.error("[profile] /users/me threw:", err);
    return null;
  }
}

async function getUploads(token: string): Promise<{ files: UploadedFile[]; error?: string }> {
  try {
    const res = await backendFetch("/files", token);
    if (!res.ok) {
      const body = await res.text().catch(() => "");
      console.error(`[profile] /files failed: ${res.status} ${body}`);
      return { files: [], error: `HTTP ${res.status}` };
    }
    return { files: (await res.json()) as UploadedFile[] };
  } catch (err) {
    console.error("[profile] /files threw:", err);
    return { files: [], error: String(err) };
  }
}

async function getStorageUsage(token: string): Promise<{ usedBytes: number; maxBytes: number } | null> {
  try {
    const res = await backendFetch("/storage/usage", token);
    if (!res.ok) return null;
    return (await res.json()) as { usedBytes: number; maxBytes: number };
  } catch {
    return null;
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

function IconForMime({ mime, name }: { mime: string; name: string }) {
  const ext = name.split(".").pop()?.toLowerCase() ?? "";
  const lower = mime.toLowerCase();

  let Icon = FaFile;
  if (lower === "application/pdf") Icon = FaFilePdf;
  else if (lower.includes("presentation") || ext === "ppt" || ext === "pptx") Icon = FaFilePowerpoint;
  else if (lower.includes("word") || ext === "doc" || ext === "docx") Icon = FaFileWord;
  else if (lower.includes("sheet") || lower.includes("excel") || ext === "xls" || ext === "xlsx" || ext === "csv") Icon = FaFileExcel;
  else if (lower.startsWith("audio/")) Icon = FaFileAudio;
  else if (lower === "application/zip" || lower === "application/x-7z-compressed" || lower === "application/x-rar-compressed" || ext === "zip" || ext === "rar" || ext === "7z") Icon = FaFileArchive;
  else if (lower.startsWith("text/") || lower.includes("json") || lower.includes("xml")) Icon = FaFileAlt;
  else if (ext === "js" || ext === "ts" || ext === "tsx" || ext === "py" || ext === "java" || ext === "go" || ext === "rs") Icon = FaFileCode;

  return (
    <div className="w-full h-full flex items-center justify-center bg-mainblack">
      <Icon size={72} className="text-mainwhite/70" />
    </div>
  );
}

function Preview({ file, ownerId }: { file: UploadedFile; ownerId: string }) {
  const raw = `${viewPath(file, ownerId)}/raw`;
  const mime = file.mime?.toLowerCase() ?? "";

  if (mime.startsWith("image/")) {
    return (
      // eslint-disable-next-line @next/next/no-img-element
      <img
        src={raw}
        alt={file.name}
        loading="lazy"
        className="w-full h-full object-cover"
      />
    );
  }

  if (mime.startsWith("video/")) {
    return (
      <video
        src={`${raw}#t=0.1`}
        muted
        playsInline
        preload="metadata"
        className="w-full h-full object-cover"
      />
    );
  }

  return <IconForMime mime={file.mime} name={file.name} />;
}

export default async function ProfilePage() {
  const token = await getSessionToken();
  if (!token) redirect("/api/auth/google");

  const backendUser = await getBackendUser(token);
  if (!backendUser || !backendUser.id) redirect("/api/auth/google");

  const name = backendUser.name && backendUser.name.length > 0 ? backendUser.name : "FileSpark User";
  const email = backendUser.email ?? "";
  const picture = backendUser.picture ?? null;
  const ownerId = backendUser.id;

  const { files: uploads, error: uploadsError } = await getUploads(token);
  const usage = await getStorageUsage(token);
  const usedBytes = usage?.usedBytes ?? 0;
  const maxBytes = usage?.maxBytes ?? 0;
  const usedPercent = maxBytes > 0 ? Math.min(100, (usedBytes / maxBytes) * 100) : 0;

  return (
    <main className="min-h-[calc(100vh-80px)] flex justify-center px-6 py-12">

      <div className="w-full max-w-5xl">

        <section className="flex items-center gap-6 rounded-lg border border-maingrey bg-mainblack/60 p-6 mb-8">

          <div className="rounded-full border border-maingrey overflow-hidden w-20 h-20 flex items-center justify-center bg-mainblack shrink-0">
            {picture ? (
              // eslint-disable-next-line @next/next/no-img-element
              <img
                src={picture}
                alt={name}
                width={80}
                height={80}
                referrerPolicy="no-referrer"
                className="w-full h-full object-cover"
              />
            ) : (
              <FaUserCircle size={64} className="text-mainwhite" />
            )}
          </div>

          <div className="flex flex-col min-w-0 flex-1">
            <span className="text-2xl font-medium text-mainwhite truncate">{name}</span>
            {email && <span className="text-mainwhite/70 truncate">{email}</span>}
          </div>

          <form action="/api/auth/logout" method="post" className="shrink-0">
            <button
              type="submit"
              className="rounded-md border border-maingrey px-4 py-2 text-mainwhite hover:border-mainorange hover:text-mainorange transition"
            >
              Log out
            </button>
          </form>

        </section>

        {usage && (
          <section className="rounded-lg border border-maingrey bg-mainblack/60 p-5 mb-6">

            <div className="flex items-baseline justify-between mb-3">
              <h2 className="text-lg font-semibold text-mainwhite">Storage</h2>
              <span className="text-mainwhite/70 text-sm">
                {formatBytes(usedBytes)} / {formatBytes(maxBytes)}
                <span className="text-mainwhite/50"> · {usedPercent.toFixed(0)}%</span>
              </span>
            </div>

            <div className="w-full h-4 rounded-full bg-maingrey overflow-hidden">
              <div
                className={
                  "h-full transition-all " +
                  (usedPercent >= 90 ? "bg-red-500" : usedPercent >= 70 ? "bg-yellow-500" : "bg-mainorange")
                }
                style={{ width: `${usedPercent}%` }}
              />
            </div>

          </section>
        )}

        <section>

          <div className="flex items-baseline justify-between mb-4">
            <h2 className="text-2xl font-semibold text-mainwhite">Uploads</h2>
            <span className="text-mainwhite/60 text-sm">
              {uploads.length} {uploads.length === 1 ? "file" : "files"}
            </span>
          </div>

          {uploads.length === 0 ? (
            <p className="text-mainwhite/60">
              {uploadsError ? `Could not load uploads (${uploadsError}).` : "No uploads yet."}
            </p>
          ) : (
            <ul className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
              {uploads.map((file) => (
                <li key={file.id} className="relative">
                  <a
                    href={viewPath(file, ownerId)}
                    target="_blank"
                    rel="noreferrer"
                    title={file.name}
                    className="block rounded-lg border border-maingrey bg-mainblack/60 overflow-hidden hover:border-mainorange transition"
                  >
                    <div className="aspect-square w-full overflow-hidden bg-mainblack">
                      <Preview file={file} ownerId={ownerId} />
                    </div>
                    <div className="p-2">
                      <div className="text-mainwhite text-sm truncate">{file.name}</div>
                      <div className="text-mainwhite/60 text-xs">{formatBytes(file.sizeBytes)}</div>
                    </div>
                  </a>
                  <a
                    href={`${viewPath(file, ownerId)}/download`}
                    title="Download"
                    aria-label="Download"
                    className="absolute top-1 right-9 rounded-md p-1.5 bg-mainblack/70 border border-maingrey text-mainwhite hover:text-mainorange hover:border-mainorange transition"
                  >
                    <FaDownload size={12} />
                  </a>
                  <DeleteUploadButton fileId={file.id} name={file.name} />
                </li>
              ))}
            </ul>
          )}

        </section>

      </div>

    </main>
  );
}
