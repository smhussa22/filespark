import { NextRequest, NextResponse } from "next/server";

export async function PATCH(
  req: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  const { id } = await params;

  const token = req.cookies.get("ga_session")?.value;
  if (!token) return NextResponse.json({ error: "unauthenticated" }, { status: 401 });

  const backend = process.env.BACKEND_URL;
  if (!backend) return NextResponse.json({ error: "backend_not_configured" }, { status: 500 });

  const body = await req.json().catch(() => ({}));
  const visibility = typeof body?.visibility === "string" ? body.visibility : "";

  const upstream = await fetch(
    `${backend.replace(/\/$/, "")}/files/${encodeURIComponent(id)}/visibility`,
    {
      method: "PATCH",
      headers: {
        Authorization: `Bearer ${token}`,
        "content-type": "application/json",
      },
      body: JSON.stringify({ visibility }),
      cache: "no-store",
    }
  );

  const text = await upstream.text();
  return new NextResponse(text, {
    status: upstream.status,
    headers: { "content-type": upstream.headers.get("content-type") ?? "application/json" },
  });
}
