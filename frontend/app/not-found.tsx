import Link from "next/link";
import { FaQuestionCircle, FaHome } from "react-icons/fa";

export default function NotFound() {
  return (
    <main className="min-h-[calc(100vh-80px)] flex items-center justify-center px-6 py-12">
      <div className="max-w-xl flex flex-col items-center text-center gap-6">

        <FaQuestionCircle size={96} className="text-mainorange" />

        <div>
          <h1 className="text-4xl md:text-5xl font-bold text-white">
            Page not found
          </h1>
          <p className="mt-3 text-lg text-white/70">
            The link you followed doesn&apos;t exist, or it may have been removed.
          </p>
        </div>

        <Link
          href="/"
          className="w-70 justify-center bg-mainorange/70 hover:bg-mainorange px-6 py-4 text-lg rounded-md flex items-center gap-3 font-medium text-mainwhite transition"
        >
          <FaHome size={28} />
          Go to home
        </Link>

      </div>
    </main>
  );
}
