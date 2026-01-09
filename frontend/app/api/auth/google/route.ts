import { NextResponse } from "next/server";

export function GET() {
  const params = new URLSearchParams({
    client_id: process.env.GOOGLE_CLIENT_ID!,
    redirect_uri: process.env.NEXT_PUBLIC_REDIRECT_URI!,
    response_type: "code",
    scope: "openid email profile",
    prompt: "consent",
  });

  return NextResponse.redirect(
    "https://accounts.google.com/o/oauth2/v2/auth?" + params.toString()
  );
}
