import { cookies } from "next/headers";
import { jwtVerify } from "jose";

const sessionSecret = new TextEncoder().encode(process.env.SESSION_SECRET!);

export async function getCurrentUser() {
  try {
    const cookieStore = await cookies();
    const token = cookieStore.get("ga_session")?.value;
    if (!token) return null;

    const { payload } = await jwtVerify(token, sessionSecret);
    return payload;
  } catch {
    return null;
  }
}
