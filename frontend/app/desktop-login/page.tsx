import { cookies } from "next/headers";
import { redirect } from "next/navigation";
import DesktopHandoff from "./DesktopHandoff";

export const dynamic = "force-dynamic";

export default async function DesktopLoginPage({
  searchParams,
}: {
  searchParams: Promise<{ port?: string }>;
}) {
  const { port } = await searchParams;

  if (!port || !/^\d+$/.test(port)) {
    return (
      <main className="min-h-[calc(100vh-80px)] flex items-center justify-center px-6 py-12">
        <div className="max-w-md text-center">
          <h1 className="text-2xl font-semibold text-mainwhite mb-2">Missing port</h1>
          <p className="text-mainwhite/70">
            Open FileSpark Desktop and click &ldquo;Continue with Google&rdquo; to start the sign-in flow.
          </p>
        </div>
      </main>
    );
  }

  const cookieStore = await cookies();
  const token = cookieStore.get("ga_session")?.value;

  if (!token) {
    redirect(`/api/auth/google?next=${encodeURIComponent(`/desktop-login?port=${port}`)}`);
  }

  return <DesktopHandoff port={port} />;
}
