"use client";

import Image from "next/image";
import { useSession } from "./hooks/useSession";

export default function Home() {
  const { user, loading } = useSession();

  if (loading) return <div>Loading...</div>;

  return (
    <div className="flex flex-col items-center mt-8">
      {user ? (
        <div className="p-4 border rounded-md shadow-md flex flex-col items-center gap-4">
          <Image
            src={user.picture}
            alt="Profile"
            width={96}
            height={96}
            className="rounded-full border"
          />

          <div className="text-center">
            <p>
              <b>Name:</b> {user.name}
            </p>
            <p>
              <b>Email:</b> {user.email}
            </p>
            <p>
              <b>User ID:</b> {user.id}
            </p>
          </div>
        </div>
      ) : (
        <div>You are not logged in</div>
      )}
    </div>
  );
}
