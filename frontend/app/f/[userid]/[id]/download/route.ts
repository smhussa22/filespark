import { NextRequest, NextResponse } from "next/server";

export async function GET(
  req: NextRequest,
  { params }: { params: Promise<{ userid: string; id: string }> }
) {
  const { userid, id } = await params;
  const backend = process.env.BACKEND_URL;
  if (!backend) return NextResponse.json({ error: "backend_not_configured" }, { status: 500 });

  const token = req.cookies.get("ga_session")?.value;
  const headers: Record<string, string> = {};
  if (token) headers.Authorization = `Bearer ${token}`;

  const upstream = await fetch(
    `${backend.replace(/\/$/, "")}/f/${encodeURIComponent(userid)}/${encodeURIComponent(id)}/download`,
    { method: "GET", headers, redirect: "manual" }
  );

  const location = upstream.headers.get("location");
  if ((upstream.status === 302 || upstream.status === 307) && location) {
    return NextResponse.redirect(location, { status: 307 });
  }

  return new NextResponse(null, { status: upstream.status });
}
