import { NextRequest, NextResponse } from "next/server";

export async function GET(req: NextRequest) {
  const backend = process.env.BACKEND_URL;
  if (!backend) {
    return NextResponse.json(
      { error: "backend_not_configured" },
      { status: 500 },
    );
  }

  const token = req.cookies.get("ga_session")?.value;
  if (!token) {
    return NextResponse.json(
      { error: "not_authenticated" },
      { status: 401 },
    );
  }

  let userRes: Response;
  try {
    userRes = await fetch(`${backend.replace(/\/$/, "")}/users/me`, {
      headers: { Authorization: `Bearer ${token}` },
      cache: "no-store",
    });
  } catch (err) {
    console.error("[desktop-handoff] backend unreachable:", err);
    return NextResponse.json(
      { error: "backend_unreachable", detail: String(err) },
      { status: 502 },
    );
  }

  if (!userRes.ok) {
    const detail = await userRes.text().catch(() => "");
    console.error(
      `[desktop-handoff] /users/me returned ${userRes.status}: ${detail}`,
    );
    // Surface the upstream status so the desktop handoff page can show why it failed
    // instead of always reporting 502.
    return NextResponse.json(
      {
        error: `user_fetch_failed_${userRes.status}`,
        upstreamStatus: userRes.status,
        detail: detail.slice(0, 500),
      },
      { status: userRes.status === 401 ? 401 : 502 },
    );
  }

  const user = await userRes.json();
  return NextResponse.json({ token, user });
}
