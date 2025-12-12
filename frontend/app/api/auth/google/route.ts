import { NextResponse } from "next/server";

export async function GET(req: Request) {
  const url = new URL(req.url);

  const base = process.env.NEXT_PUBLIC_BASE_URL!;
  const nextParam = url.searchParams.get("next") ?? "/";
  const next = new URL(nextParam, base).toString();

  const state = Buffer.from(JSON.stringify({ next })).toString("base64url");

  const parameters = new URLSearchParams({
    client_id: process.env.GOOGLE_CLIENT_ID!,
    redirect_uri: process.env.GOOGLE_REDIRECT_URI!,
    response_type: "code",
    scope: ["openid", "email", "profile"].join(" "),
    access_type: "offline",
    include_granted_scopes: "true",
    prompt: "consent",
    state,
  });

  return NextResponse.redirect(
    "https://accounts.google.com/o/oauth2/v2/auth?" + parameters.toString(),
  );
}
