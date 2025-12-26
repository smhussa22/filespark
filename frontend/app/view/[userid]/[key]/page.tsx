import React from "react";

export default async function ViewerPage(props: any) {
  const { userId, key } = await props.params;

  const response = await fetch(
    `http://localhost:8000/api/file/${key}`,
    { cache: "no-store" }
  );

  if (!response.ok) {
    return <h1>Failed To Load Response</h1>;
  }

  const data = await response.json();
  const mime = data.mime;
  const url = data.signedUrl;

  return (
    <div className="bg-black flex items-center justify-center min-h-screen p-10">
      {mime.startsWith("video/") && (
        <video
          src={url}
          controls
          autoPlay
          className="w-full max-w-4xl rounded-xl shadow-lg"
        />
      )}

      {mime.startsWith("image/") && (
        <img
          src={url}
          alt={key}
          className="max-w-3xl rounded-xl shadow-lg"
        />
      )}

      {mime.startsWith("audio/") && (
        <audio controls src={url} className="w-full max-w-lg" />
      )}

      {!mime.startsWith("video/") &&
        !mime.startsWith("image/") &&
        !mime.startsWith("audio/") && (
          <a
            href={url}
            className="text-white underline text-xl"
            target="_blank"
          >
            Download {key}
          </a>
        )}
    </div>
  );
}
