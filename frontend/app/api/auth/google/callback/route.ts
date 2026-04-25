import { NextRequest, NextResponse } from "next/server";

export async function GET(req: NextRequest) {
  const reqUrl = new URL(req.url);
  const home = new URL("/", reqUrl);
  const code = reqUrl.searchParams.get("code");

  const nextPath = req.cookies.get("oauth_next")?.value;
  const destination = nextPath && nextPath.startsWith("/") ? new URL(nextPath, reqUrl) : home;

  if (!code) {
    const r = NextResponse.redirect(destination);
    r.cookies.delete("oauth_next");
    return r;
  }

  const res = await fetch(
    `${process.env.BACKEND_URL}/auth/google/web/callback`,
    {
      method: "POST",
      headers: { "content-type": "application/json" },
      body: JSON.stringify({ code }),
    }
  );

  if (!res.ok) {
    const r = NextResponse.redirect(home);
    r.cookies.delete("oauth_next");
    return r;
  }

  const { token } = await res.json();

  const response = NextResponse.redirect(destination);
  response.cookies.set("ga_session", token, {
    httpOnly: true,
    secure: process.env.NODE_ENV === "production",
    sameSite: "lax",
    path: "/",
    maxAge: 60 * 60 * 24 * 7,
  });
  response.cookies.delete("oauth_next");

  return response;
}
