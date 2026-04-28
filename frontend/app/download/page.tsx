import Link from "next/link";
import { FaDownload, FaWindows, FaJava } from "react-icons/fa";

export const metadata = {
  title: "Download · FileSpark",
  description: "Download the FileSpark desktop client.",
};

export default function DownloadPage() {
  return (
    <main className="min-h-[calc(100vh-80px)] flex justify-center px-6 py-12">

      <div className="w-full max-w-3xl">

        <h1 className="text-4xl font-semibold text-mainwhite mb-2">Download FileSpark</h1>
        <p className="text-mainwhite/70 mb-10">
          Install the desktop client to upload from anywhere with a global hotkey, watch folders for new files, and
          drop screenshots straight into a sharable link.
        </p>

        {/* Primary: bundled Windows zip — no Java install required */}
        <section className="rounded-lg border border-mainorange/40 bg-mainblack/60 p-8 flex flex-col items-center text-center gap-4">

          <FaWindows size={56} className="text-mainorange" />

          <div>
            <div className="text-mainwhite text-2xl font-medium">FileSpark for Windows</div>
            <div className="text-mainwhite/60 text-sm mt-1">
              Self-contained · no Java installation required
            </div>
          </div>

          <p className="text-mainwhite/70 max-w-md">
            Bundled with its own runtime. Unzip and run <code className="text-mainorange">FileSpark.exe</code>.
          </p>

          <a
            href="/downloads/filespark-windows.zip"
            download="filespark-windows.zip"
            className="mt-2 rounded-md bg-mainorange/80 hover:bg-mainorange px-6 py-3 text-mainwhite font-medium flex items-center gap-2 transition"
          >
            <FaDownload /> Download for Windows
          </a>

          <p className="text-mainwhite/40 text-xs">
            ~50 MB · zip · contains FileSpark.exe and a bundled JRE
          </p>

        </section>

        {/* macOS .app bundle — drag to Applications, shows up as FileSpark in
            Accessibility settings (the bare jar gets listed as "java"). */}
        <section className="mt-10 rounded-lg border border-maingrey bg-mainblack/60 p-6 flex flex-col items-center text-center gap-3">

          <div className="text-mainwhite text-lg font-medium">FileSpark for macOS</div>
          <p className="text-mainwhite/60 text-sm max-w-md">
            Unzip and drag <code className="text-mainorange">FileSpark.app</code> into your Applications folder.
            Requires Java 17 or newer with JavaFX (e.g.{" "}
            <a className="text-mainorange hover:underline" href="https://www.azul.com/downloads/?package=jdk-fx" target="_blank" rel="noreferrer">
              Azul Zulu FX
            </a>).
            On first launch, grant FileSpark Accessibility permission in
            System Settings → Privacy &amp; Security → Accessibility so global hotkeys work.
          </p>

          <a
            href="/downloads/filespark-macos.zip"
            download="filespark-macos.zip"
            className="mt-1 rounded-md border border-maingrey px-5 py-2.5 text-mainwhite hover:border-mainorange hover:text-mainorange transition flex items-center gap-2 text-sm"
          >
            <FaDownload /> Download for macOS
          </a>

        </section>

        {/* Plain cross-platform jar for Linux / advanced users */}
        <section className="mt-10 rounded-lg border border-maingrey bg-mainblack/60 p-6 flex flex-col items-center text-center gap-3">

          <FaJava size={36} className="text-mainwhite/70" />

          <div className="text-mainwhite text-lg font-medium">Linux or other platforms</div>
          <p className="text-mainwhite/60 text-sm max-w-md">
            Run the cross-platform JAR directly. Requires Java 17 or newer with JavaFX (e.g.{" "}
            <a className="text-mainorange hover:underline" href="https://www.azul.com/downloads/?package=jdk-fx" target="_blank" rel="noreferrer">
              Azul Zulu FX
            </a>).
          </p>

          <a
            href="/downloads/filespark.jar"
            download="filespark.jar"
            className="mt-1 rounded-md border border-maingrey px-5 py-2.5 text-mainwhite hover:border-mainorange hover:text-mainorange transition flex items-center gap-2 text-sm"
          >
            <FaDownload /> Download filespark.jar (~20 MB)
          </a>

        </section>

        <section className="mt-10 rounded-lg border border-maingrey bg-mainblack/60 p-6">

          <h2 className="text-2xl font-semibold text-mainwhite mb-3">Build from source</h2>
          <p className="text-mainwhite/70 mb-4">
            The client is open source. Clone the repo and run it locally with Maven.
          </p>
          <pre className="text-xs text-mainwhite/80 bg-mainblack rounded-md p-3 overflow-x-auto">
{`git clone https://github.com/smhussa22/filespark
cd filespark/client
mvn javafx:run`}
          </pre>

        </section>

        <p className="text-mainwhite/60 text-sm mt-10 text-center">
          Looking for the web app? <Link href="/" className="text-mainorange hover:underline">Go to the homepage</Link>.
        </p>

      </div>

    </main>
  );
}
