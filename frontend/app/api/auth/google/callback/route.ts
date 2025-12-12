import { NextResponse, type NextRequest } from "next/server";
import { SignJWT } from "jose";

const sessionSecret = new TextEncoder().encode(process.env.SESSION_SECRET!);

export async function GET(req: NextRequest) {
  try {
    const url = new URL(req.url);
    const code = url.searchParams.get("code");
    const stateRaw = url.searchParams.get("state");

    if (!code || !stateRaw)
      return NextResponse.redirect(process.env.NEXT_PUBLIC_BASE_URL!);

    const parsed = JSON.parse(Buffer.from(stateRaw, "base64url").toString());

    const nextPath = typeof parsed?.next === "string" ? parsed.next : "/";

    const tokenResponse = await fetch("https://oauth2.googleapis.com/token", {
      method: "POST",
      headers: { "content-type": "application/x-www-form-urlencoded" },
      body: new URLSearchParams({
        code,
        client_id: process.env.GOOGLE_CLIENT_ID!,
        client_secret: process.env.GOOGLE_CLIENT_SECRET!,
        grant_type: "authorization_code",
        redirect_uri: process.env.GOOGLE_REDIRECT_URI!,
      }),
    });

    const token = await tokenResponse.json();

    const payload = JSON.parse(
      Buffer.from(token.id_token.split(".")[1], "base64").toString(),
    );

    const userId = payload.sub;
    const email = payload.email;
    const name = payload.name;
    const picture = payload.picture;
    console.log(picture);

    if (!userId) {
      return NextResponse.redirect(process.env.NEXT_PUBLIC_BASE_URL!);
    }

    const jwt = await new SignJWT({ userId, email, name, picture })
      .setProtectedHeader({ alg: "HS256" })
      .setIssuedAt()
      .setExpirationTime("7d")
      .sign(sessionSecret);

    const redirectUrl = new URL(nextPath, process.env.NEXT_PUBLIC_BASE_URL!);

    const res = NextResponse.redirect(redirectUrl.toString());

    res.cookies.set("ga_session", jwt, {
      httpOnly: true,
      secure: process.env.NODE_ENV === "production",
      sameSite: "lax",
      path: "/",
      maxAge: 7 * 24 * 60 * 60,
    });

    return res;
  } catch (err) {
    console.error("Google OAuth ERROR:", err);
    return NextResponse.redirect(process.env.NEXT_PUBLIC_BASE_URL!);
  }
}
