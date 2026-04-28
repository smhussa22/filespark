package com.filespark.os;

import com.filespark.OperatingSystem;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public final class HotkeyUtil {

    public static final int MAX_KEYS = 3;
    public static final int MAX_MODIFIERS = MAX_KEYS - 1;

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

        return new Hotkey(normalizeMask(rawMask), NativeKeyEvent.VC_U);

    }

    /**
     * Collapses jnativehook's L/R modifier bits into a single canonical
     * left-side bit per modifier. The previous implementation just did
     * {@code mask & 0x0F}, which silently dropped the right-side bits
     * (0x10/0x20/0x40/0x80) — so a hotkey recorded with the LEFT Cmd
     * never matched if the user later pressed RIGHT Cmd, and vice versa.
     * This was particularly painful on macOS where right-Cmd is common.
     */
    public static int normalizeMask(int rawMask) {

        int mask = 0;
        if ((rawMask & NativeKeyEvent.SHIFT_MASK) != 0) mask |= NativeKeyEvent.SHIFT_L_MASK;
        if ((rawMask & NativeKeyEvent.CTRL_MASK)  != 0) mask |= NativeKeyEvent.CTRL_L_MASK;
        if ((rawMask & NativeKeyEvent.META_MASK)  != 0) mask |= NativeKeyEvent.META_L_MASK;
        if ((rawMask & NativeKeyEvent.ALT_MASK)   != 0) mask |= NativeKeyEvent.ALT_L_MASK;
        return mask;

    }

    public static int countModifiers(int normalizedMask) {

        int count = 0;
        if ((normalizedMask & NativeKeyEvent.SHIFT_L_MASK) != 0) count++;
        if ((normalizedMask & NativeKeyEvent.CTRL_L_MASK)  != 0) count++;
        if ((normalizedMask & NativeKeyEvent.META_L_MASK)  != 0) count++;
        if ((normalizedMask & NativeKeyEvent.ALT_L_MASK)   != 0) count++;
        return count;

    }

}
