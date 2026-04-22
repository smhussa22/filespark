import ApplicationViewer from "@/app/components/ApplicationViewer";
import AudioPlayer from "@/app/components/AudioPlayer";
import FailedToLoadContent from "@/app/components/FailedToLoadContent";
import ImageDisplay from "@/app/components/ImageDisplay";
import UnsupportedContent from "@/app/components/UnsupportedContent";
import VideoPlayer from "@/app/components/VideoPlayer";
import React from "react";

export default async function FileViewerPage({
  params,
}: {
  params: Promise<{ userid: string; id: string }>;
}) {
  const { userid, id } = await params;

  const response = await fetch(
    `${process.env.BACKEND_URL}/f/${encodeURIComponent(userid)}/${encodeURIComponent(id)}/meta`,
    { cache: "no-store" }
  );

  if (!response.ok) {
    return <FailedToLoadContent />;
  }

  const data = await response.json();
  const mime: string = data.mime ?? "";
  const url: string = data.signedUrl;

  return (
    <div className="bg-black flex items-center justify-center min-h-screen p-10">
      {mime.startsWith("video/") && <VideoPlayer src={url} />}
      {mime.startsWith("image/") && <ImageDisplay src={url} fileid={id} />}
      {mime.startsWith("audio/") && <AudioPlayer src={url} />}
      {mime.startsWith("application/") && <ApplicationViewer />}
      {!mime.startsWith("video/") &&
        !mime.startsWith("image/") &&
        !mime.startsWith("audio/") &&
        !mime.startsWith("application/") && <UnsupportedContent />}
    </div>
  );
}
