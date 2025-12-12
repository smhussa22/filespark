import { NextRequest, NextResponse } from "next/server";
import { jwtVerify } from "jose";

const sessionSecret = new TextEncoder().encode(process.env.SESSION_SECRET!);

export async function GET(req: NextRequest) {
  try {
    const token = req.cookies.get("ga_session")?.value;

    if (!token) {
      return NextResponse.json({ user: null }, { status: 200 });
    }

    const { payload } = await jwtVerify(token, sessionSecret);

    return NextResponse.json({
      user: {
        id: payload.userId,
        email: payload.email,
        name: payload.name,
        picture: payload.picture,
      },
    });
  } catch {
    return NextResponse.json({ user: null }, { status: 200 });
  }
}
