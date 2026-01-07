import { useState } from "react";
import { FaRegEye, FaRegEyeSlash } from "react-icons/fa6";

export default function PrivacySwitch() {
  const [isPrivate, setIsPrivate] = useState(true);

  return (
    <label className="relative inline-flex cursor-pointer items-center">
      {/* Accessible checkbox */}
      <input
        type="checkbox"
        checked={!isPrivate}
        onChange={() => setIsPrivate(!isPrivate)}
        className="sr-only peer"
        aria-label="Toggle privacy"
      />

      {/* Track */}
      <div
        className="
          h-9 w-16 rounded-full
          bg-gray-300 dark:bg-gray-700
          peer-checked:bg-gray-400 dark:peer-checked:bg-gray-600
          transition-colors duration-200
        "
      />

      {/* Knob */}
      <div
        className="
          absolute left-1 flex h-7 w-7 items-center justify-center
          rounded-full bg-white dark:bg-gray-200
          text-gray-700
          transition-transform duration-200
          peer-checked:translate-x-7
        "
      >
        {isPrivate ? (
          <FaRegEyeSlash className="h-4 w-4" />
        ) : (
          <FaRegEye className="h-4 w-4" />
        )}
      </div>
    </label>
  );
}
