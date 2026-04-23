import { NextRequest, NextResponse } from "next/server";

export async function DELETE(
  req: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  const { id } = await params;

  const token = req.cookies.get("ga_session")?.value;
  if (!token) return NextResponse.json({ error: "unauthenticated" }, { status: 401 });

  const backend = process.env.BACKEND_URL;
  if (!backend) return NextResponse.json({ error: "backend_not_configured" }, { status: 500 });

  const upstream = await fetch(
    `${backend.replace(/\/$/, "")}/files/${encodeURIComponent(id)}`,
    {
      method: "DELETE",
      headers: { Authorization: `Bearer ${token}` },
      cache: "no-store",
    }
  );

  if (upstream.status === 200 || upstream.status === 204) {
    return new NextResponse(null, { status: 204 });
  }

  const text = await upstream.text().catch(() => "");
  return new NextResponse(text, { status: upstream.status });
}
