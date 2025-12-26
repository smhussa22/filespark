package com.filespark.os;

import com.filespark.OperatingSystem;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public final class HotkeyUtil {

    private HotkeyUtil() {}

    public static Hotkey uploadFromClipboard() {

        int rawMask;
        switch (OperatingSystem.OS) {

            case MAC:
                rawMask = NativeKeyEvent.META_MASK | NativeKeyEvent.SHIFT_MASK;
                break;

            case LINUX:
            case WINDOWS:

            default:
                rawMask = NativeKeyEvent.CTRL_MASK | NativeKeyEvent.SHIFT_MASK;
                break;

        }

        int cleanMask = rawMask & 0x0F; // @todo find a better fix than this
        return new Hotkey(cleanMask, NativeKeyEvent.VC_U);

    }

}
