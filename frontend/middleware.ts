import { NextRequest, NextResponse } from "next/server";

// Inject Authorization: Bearer <ga_session> for /f/* paths so that the rewrite
// to the backend can authenticate the owner. The browser stores the JWT in an
// httpOnly cookie; rewrites alone can't promote that cookie to a header.
export function middleware(req: NextRequest) {

  const url = req.nextUrl;
  if (!url.pathname.startsWith("/f/")) return NextResponse.next();

  const token = req.cookies.get("ga_session")?.value;
  if (!token) return NextResponse.next();

  const headers = new Headers(req.headers);
  if (!headers.has("authorization")) headers.set("authorization", `Bearer ${token}`);

  return NextResponse.next({
    request: { headers },
  });

}

export const config = {
  matcher: ["/f/:path*"],
};
