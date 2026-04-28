import Link from "next/link";
import { FaDownload, FaJava } from "react-icons/fa";

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

        <section className="rounded-lg border border-maingrey bg-mainblack/60 p-8 flex flex-col items-center text-center gap-4">

          <FaJava size={56} className="text-mainorange" />

          <div>
            <div className="text-mainwhite text-2xl font-medium">FileSpark Desktop</div>
            <div className="text-mainwhite/60 text-sm mt-1">
              Cross-platform · Windows, macOS, Linux · requires Java 17+
            </div>
          </div>

          <p className="text-mainwhite/70 max-w-md">
            A single self-contained JAR. Download it and double-click, or run{" "}
            <code className="text-mainorange">java -jar filespark.jar</code>.
          </p>

          <a
            href="/downloads/filespark.jar"
            download="filespark.jar"
            className="mt-2 rounded-md bg-mainorange/80 hover:bg-mainorange px-6 py-3 text-mainwhite font-medium flex items-center gap-2 transition"
          >
            <FaDownload /> Download filespark.jar
          </a>

          <p className="text-mainwhite/40 text-xs">
            ~16 MB · SHA-256 published in release notes
          </p>

        </section>

        <section className="mt-10 rounded-lg border border-maingrey bg-mainblack/60 p-6">

          <h2 className="text-2xl font-semibold text-mainwhite mb-3">Don&apos;t have Java?</h2>
          <p className="text-mainwhite/70 mb-3">
            Install a JDK (any vendor, version 17 or newer):
          </p>
          <ul className="text-mainwhite/80 text-sm list-disc list-inside space-y-1 mb-4">
            <li>
              <a className="text-mainorange hover:underline" href="https://adoptium.net/temurin/releases/?version=21" target="_blank" rel="noreferrer">
                Eclipse Temurin
              </a> &mdash; recommended, free
            </li>
            <li>
              <a className="text-mainorange hover:underline" href="https://www.azul.com/downloads/?package=jdk-fx" target="_blank" rel="noreferrer">
                Azul Zulu (FX bundle)
              </a> &mdash; ships JavaFX modules
            </li>
          </ul>
          <p className="text-mainwhite/60 text-sm">
            Once Java is on your PATH, double-click the JAR or run it from a terminal.
          </p>

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
