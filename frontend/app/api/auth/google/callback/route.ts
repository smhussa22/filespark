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

  const backendUrl = process.env.BACKEND_URL;
  if (!backendUrl) {
    console.error("[oauth-callback] BACKEND_URL env var is not set");
    const r = NextResponse.redirect(new URL("/?auth_error=backend_unset", reqUrl));
    r.cookies.delete("oauth_next");
    return r;
  }

  let token: string | undefined;
  try {
    const res = await fetch(`${backendUrl}/auth/google/web/callback`, {
      method: "POST",
      headers: { "content-type": "application/json" },
      body: JSON.stringify({ code }),
    });

    if (!res.ok) {
      const text = await res.text().catch(() => "");
      console.error("[oauth-callback] backend returned non-OK", res.status, text);
      const r = NextResponse.redirect(new URL("/?auth_error=backend_error", reqUrl));
      r.cookies.delete("oauth_next");
      return r;
    }

    ({ token } = await res.json());
  } catch (err) {
    console.error("[oauth-callback] backend fetch threw", err);
    const r = NextResponse.redirect(new URL("/?auth_error=backend_unreachable", reqUrl));
    r.cookies.delete("oauth_next");
    return r;
  }

  if (!token) {
    const r = NextResponse.redirect(new URL("/?auth_error=missing_token", reqUrl));
    r.cookies.delete("oauth_next");
    return r;
  }

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
