import Link from "next/link";

export default function DisclaimerBanner() {
  return (
    <div className="w-full bg-mainorange/10 border-b border-mainorange/30 px-4 py-3 text-center text-sm text-mainwhite/90">
      <div className="mx-auto max-w-4xl leading-snug">
        <p>
          Thank you to everyone who signed up and gave FileSpark a try, it really meant a lot. The
          hosted version is now being shut down to keep AWS costs manageable, but if FileSpark was
          useful to you, you can still download and run a build locally from the{" "}
          <Link
            href="https://github.com/smhussa22/filespark"
            className="font-medium text-mainorange underline-offset-2 hover:underline"
          >
            GitHub repository
          </Link>
          . Thanks again for being part of it.
        </p>
      </div>
    </div>
  );
}
