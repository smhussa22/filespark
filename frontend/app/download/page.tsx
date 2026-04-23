import Link from "next/link";
import { FaDownload } from "react-icons/fa";

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

          <FaDownload size={56} className="text-mainorange" />

          <div>
            <div className="text-mainwhite text-2xl font-medium">FileSpark Desktop</div>
            <div className="text-mainwhite/60 text-sm mt-1">Cross-platform · one build for Windows, macOS, and Linux</div>
          </div>

          <p className="text-mainwhite/70 max-w-md">
            A single self-contained download. Nothing else to install.
          </p>

          <button
            type="button"
            disabled
            className="mt-2 rounded-md border border-maingrey px-6 py-3 text-mainwhite/60 cursor-not-allowed flex items-center gap-2"
            title="Coming soon"
          >
            <FaDownload /> Download (coming soon)
          </button>

        </section>

        <section className="mt-10 rounded-lg border border-maingrey bg-mainblack/60 p-6">

          <h2 className="text-2xl font-semibold text-mainwhite mb-3">Build from source</h2>
          <p className="text-mainwhite/70 mb-4">
            The client is open source. Clone the repo and run it locally with Maven.
          </p>
          <pre className="text-xs text-mainwhite/80 bg-mainblack rounded-md p-3 overflow-x-auto">
{`git clone https://github.com/yourname/filespark
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
