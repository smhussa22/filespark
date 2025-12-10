import Image from 'next/image';
import { BsGoogle } from "react-icons/bs";

export default function LogInCard() {
  return (
    <div className="
      w-full max-w-md
      rounded-2xl
      bg-maingrey/70
      backdrop-blur-md
      border border-white/10
      shadow-2xl
      px-8 pt-10 pb-8
      flex flex-col items-center gap-8
      text-mainwhite
    ">

      {/* Logo + Title */}
      <div className="flex flex-col items-center gap-4">
        <Image
          draggable={false}
          src="/icon.svg"
          alt="FileSpark Logo"
          width={96}
          height={96}
        />
        <h1 className="text-3xl font-bold tracking-tight">
          Welcome to <span className="text-mainorange">FileSpark</span>
        </h1>
      </div>

      {/* Google Button */}
      <button
        className="
          w-full
          flex items-center justify-center gap-3
          rounded-xl
          bg-white text-neutral-900
          py-4
          text-lg font-semibold
          shadow-md
          hover:shadow-lg hover:-translate-y-0.5
          transition-all duration-200
          active:translate-y-0
        "
      >
        <BsGoogle size={22} />
        Continue with Google
      </button>

      {/* Helper Text */}
      <p className="text-sm text-mainwhite/60 text-center leading-relaxed">
        FileSpark currently supports Google accounts only.
      </p>
    </div>
  );
}
