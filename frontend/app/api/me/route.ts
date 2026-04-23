import { NextRequest, NextResponse } from "next/server";

export async function GET(req: NextRequest) {
  const token = req.cookies.get("ga_session")?.value;

  if (!token) {
    return NextResponse.json({ user: null });
  }

  const backendRes = await fetch(`${process.env.BACKEND_URL}/users/me`, {
    headers: { Authorization: `Bearer ${token}` },
    cache: "no-store",
  });

  if (!backendRes.ok) {
    return NextResponse.json({ user: null });
  }

  const user = await backendRes.json();
  return NextResponse.json({ user });
}
