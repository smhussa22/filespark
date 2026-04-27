type Stats = { totalUsers: number; totalUploads: number };

async function getStats(): Promise<Stats> {

  const fallback: Stats = { totalUsers: 0, totalUploads: 0 };
  const backend = process.env.BACKEND_URL?.replace(/\/$/, "");
  if (!backend) return fallback;

  try {
    const res = await fetch(`${backend}/stats`, { next: { revalidate: 60 } });
    if (!res.ok) return fallback;
    const json = (await res.json()) as Partial<Stats>;
    return {
      totalUsers: typeof json.totalUsers === "number" ? json.totalUsers : 0,
      totalUploads: typeof json.totalUploads === "number" ? json.totalUploads : 0,
    };
  } catch {
    return fallback;
  }

}

export async function StatsSection() {

  const stats = await getStats();

  return (

    <section className="relative z-20 bg-mainblack">

      <div className="mx-auto max-w-7xl px-6 py-16">

        <div className="grid grid-cols-1 md:grid-cols-3 gap-10 text-center">

          <div>

            <div className="text-3xl font-bold text-white">

              {stats.totalUploads.toLocaleString()}

            </div>

            <div className="mt-2 text-sm text-mainorange">

              files uploaded

            </div>

          </div>

          <div>

            <div className="text-3xl font-bold text-white">
              {stats.totalUsers.toLocaleString()}
            </div>

            <div className="mt-2 text-sm text-mainorange">
              users connected
            </div>

          </div>

          <div>
            <div className="text-3xl font-bold text-white">
              12+
            </div>
            <div className="mt-2 text-sm text-mainorange">
              supported file types
            </div>
          </div>

        </div>
      </div>
    </section>
  );
}
