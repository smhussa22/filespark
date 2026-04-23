import { NextRequest, NextResponse } from "next/server";

export async function GET(req: NextRequest) {
  const token = req.cookies.get("ga_session")?.value;
  if (!token) return NextResponse.json({ files: [] }, { status: 401 });

  const backendRes = await fetch(`${process.env.BACKEND_URL}/files`, {
    headers: { Authorization: `Bearer ${token}` },
    cache: "no-store",
  });

  if (!backendRes.ok) {
    return NextResponse.json({ files: [] }, { status: backendRes.status });
  }

  const files = await backendRes.json();
  return NextResponse.json({ files });
}
