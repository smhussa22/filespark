import { NextRequest, NextResponse } from "next/server";

// Streams a file from the backend with Content-Disposition: attachment so the
// browser downloads instead of navigating. Uses the existing /raw endpoint, so
// the response is a 302 to a signed S3 URL — fetch follows it server-side and
// streams the bytes back through this proxy with download headers we control.
export async function GET(
  req: NextRequest,
  ctx: { params: Promise<{ userid: string; id: string }> },
) {

  const backend = process.env.BACKEND_URL;
  if (!backend) return new NextResponse("backend_not_configured", { status: 500 });

  const { userid, id } = await ctx.params;
  const token = req.cookies.get("ga_session")?.value;

  const headers: Record<string, string> = {};
  if (token) headers.authorization = `Bearer ${token}`;

  // Hit /download (not /raw) so the backend's download counter increments. The 302
  // it returns points at a signed S3 URL; we follow it server-side and stream the
  // bytes back through this proxy with our own Content-Disposition.
  const upstream = `${backend.replace(/\/$/, "")}/f/${encodeURIComponent(userid)}/${encodeURIComponent(id)}/download`;

  const upstreamRes = await fetch(upstream, {
    headers,
    redirect: "follow",
    cache: "no-store",
  });

  if (!upstreamRes.ok || !upstreamRes.body) {
    return new NextResponse(`upstream_${upstreamRes.status}`, { status: upstreamRes.status });
  }

  // Try to recover the original filename from the URL hint (?filename=) or fall back to id.
  const urlObj = new URL(req.url);
  const hinted = urlObj.searchParams.get("filename");
  const filename = hinted || `${id}`;
  const encoded = encodeURIComponent(filename).replace(/'/g, "%27");

  const responseHeaders = new Headers();
  responseHeaders.set(
    "content-type",
    upstreamRes.headers.get("content-type") ?? "application/octet-stream",
  );
  const len = upstreamRes.headers.get("content-length");
  if (len) responseHeaders.set("content-length", len);
  responseHeaders.set(
    "content-disposition",
    `attachment; filename*=UTF-8''${encoded}`,
  );
  responseHeaders.set("cache-control", "private, no-store");

  return new NextResponse(upstreamRes.body, {
    status: 200,
    headers: responseHeaders,
  });

}
