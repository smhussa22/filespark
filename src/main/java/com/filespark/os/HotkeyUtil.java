package com.filespark.os;

import com.filespark.OperatingSystem;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public final class HotkeyUtil {

    private HotkeyUtil() {}

    public static Hotkey uploadFromClipboard() {

        Hotkey hk;

        switch (OperatingSystem.OS) {

            case MAC:
                hk = new Hotkey(NativeKeyEvent.META_MASK | NativeKeyEvent.SHIFT_MASK, NativeKeyEvent.VC_V);
                break;

            case LINUX:
            case WINDOWS:
                hk = new Hotkey(NativeKeyEvent.CTRL_MASK | NativeKeyEvent.SHIFT_MASK, NativeKeyEvent.VC_V);
                break;

            default:
                hk = null;

        }

        // @debug
        System.out.println("========================================");
        System.out.println("HOTKEY CREATED");
        System.out.println("Detected OS      : " + OperatingSystem.OS);

        if (hk != null) {
            System.out.println("Modifier mask    : " + hk.modifierMask());
            System.out.println("Key code         : " + hk.keyCode());
            System.out.println("CTRL_MASK        : " + NativeKeyEvent.CTRL_MASK);
            System.out.println("SHIFT_MASK       : " + NativeKeyEvent.SHIFT_MASK);
            System.out.println("META_MASK        : " + NativeKeyEvent.META_MASK);
        } else {
            System.out.println("Hotkey is NULL");
        }

        System.out.println("========================================");

        return hk;
    }

}
