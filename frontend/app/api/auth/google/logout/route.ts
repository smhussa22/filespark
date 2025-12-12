import { NextResponse } from "next/server";

export function GET() {
  const base = process.env.NEXT_PUBLIC_BASE_URL!;
  const redirectUrl = new URL("/", base);

  const res = NextResponse.redirect(redirectUrl.toString());

  res.cookies.set("ga_session", "", {
    httpOnly: true,
    secure: process.env.NODE_ENV === "production",
    sameSite: "lax",
    path: "/",
    maxAge: 0,
  });

  return res;
}
