import { NextResponse, type NextRequest } from "next/server";

export async function GET(req: NextRequest) {
  const url = new URL(req.url);
  const code = url.searchParams.get("code");
  const stateRaw = url.searchParams.get("state");

  if (!code || !stateRaw) {
    return NextResponse.redirect(process.env.NEXT_PUBLIC_BASE_URL!);
  }

  const parsed = JSON.parse(Buffer.from(stateRaw, "base64url").toString());
  const nextPath = typeof parsed?.next === "string" ? parsed.next : "/";

  const backendRes = await fetch(
    `${process.env.BACKEND_URL}/auth/google/callback`,
    {
      method: "POST",
      headers: { "content-type": "application/json" },
      body: JSON.stringify({ code }),
    },
  );

  if (!backendRes.ok) {
    return NextResponse.redirect(process.env.NEXT_PUBLIC_BASE_URL!);
  }

  const { token } = await backendRes.json();

  const redirectUrl = new URL(nextPath, process.env.NEXT_PUBLIC_BASE_URL!);
  const res = NextResponse.redirect(redirectUrl.toString());

  res.cookies.set("ga_session", token, {
    httpOnly: true,
    secure: process.env.NODE_ENV === "production",
    sameSite: "lax",
    path: "/",
    maxAge: 7 * 24 * 60 * 60,
  });

  return res;
}
