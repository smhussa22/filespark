import Image from "next/image";
import { FaGoogle } from "react-icons/fa";

export default function LogInCard() {
  return (
    <div className="flex flex-col items-center justify-center w-96 h-auto p-6 rounded-md shadow-lg bg-maingrey">
      {/* Logo */}
      <div className="mb-4">
        <Image src="/newlogo.png" alt="FileSpark Logo" width={150} height={150} />
      </div>

      {/* Welcome Text */}
      <h1 className="text-3xl text-white font-semibold mb-4">
        Welcome To{" "}
        <span className="text-3xl font-semibold tracking-tight text-mainorange">
          FileSpark
        </span>
      </h1>

      {/* Google Login Button */}
      <button className="w-full text-white text-2xl hover:text-mainorange hover:bg-white transition bg-mainorange rounded-md flex items-center justify-center gap-3 px-4 py-2">
        <FaGoogle size={24} />
        Continue with Google
      </button>

      {/* Disclaimer */}
      <p className="mt-4 text-white text-sm text-center">
        We only support Google accounts for login.
      </p>
    </div>
  );
}