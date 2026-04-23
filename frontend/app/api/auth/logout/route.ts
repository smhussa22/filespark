import { NextRequest, NextResponse } from "next/server";

export async function POST(req: NextRequest) {
  const home = new URL("/", req.url);
  const response = NextResponse.redirect(home, { status: 303 });
  response.cookies.set("ga_session", "", {
    httpOnly: true,
    secure: process.env.NODE_ENV === "production",
    sameSite: "lax",
    path: "/",
    maxAge: 0,
  });
  return response;
}
