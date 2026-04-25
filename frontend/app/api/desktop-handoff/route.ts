import { NextRequest, NextResponse } from "next/server";

export async function GET(req: NextRequest) {
  const backend = process.env.BACKEND_URL;
  if (!backend) {
    return NextResponse.json({ error: "backend_not_configured" }, { status: 500 });
  }

  const token = req.cookies.get("ga_session")?.value;
  if (!token) {
    return NextResponse.json({ error: "not_authenticated" }, { status: 401 });
  }

  const userRes = await fetch(`${backend.replace(/\/$/, "")}/users/me`, {
    headers: { Authorization: `Bearer ${token}` },
    cache: "no-store",
  });

  if (!userRes.ok) {
    return NextResponse.json({ error: `user_fetch_failed_${userRes.status}` }, { status: 502 });
  }

  const user = await userRes.json();
  return NextResponse.json({ token, user });
}
