import { NextResponse } from "next/server";

export async function GET(req: Request) {
  const code = new URL(req.url).searchParams.get("code");
  if (!code) return NextResponse.redirect("/");

  const res = await fetch(
    `${process.env.BACKEND_URL}/auth/google/web/callback`,
    {
      method: "POST",
      headers: { "content-type": "application/json" },
      body: JSON.stringify({ code }),
    }
  );

  if (!res.ok) return NextResponse.redirect("/");

  const { token } = await res.json();

  const response = NextResponse.redirect("/");
  response.cookies.set("ga_session", token, {
    httpOnly: true,
    secure: process.env.NODE_ENV === "production",
    sameSite: "lax",
    path: "/",
    maxAge: 60 * 60 * 24 * 7,
  });

  return response;
}
