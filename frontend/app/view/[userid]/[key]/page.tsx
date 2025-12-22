import VideoPlayer from "@/app/components/VideoPlayer";
import React from "react";

export default async function ViewerPage(props: any) {
  const { userid, fileid } = await props.params;
  const response = await fetch(`http://localhost:8000/api/public/file/${userid}/${fileid}`, {
    cache: "no-store",
  });

  if (!response.ok)
    return (
      <>
        <h1>Failed To Load Response</h1>
      </>
    );

  const data = await response.json();
  const mime = data.mime;
  const url = data.signedUrl;

  return (
    <div className="flex items-center justify-center min-h-screen p-10">
      {mime.startsWith("video/") && <VideoPlayer src={url} />}

      {mime.startsWith("image/") && (
        <img src={url} alt={fileid} className="max-w-3xl rounded-xl shadow-lg" />
      )}
    </div>
  );
}
