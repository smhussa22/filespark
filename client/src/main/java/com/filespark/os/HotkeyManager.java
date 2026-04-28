package com.filespark.os;

public class HotkeyManager {

    private static Hotkey uploadFromClipboard;
    private static volatile boolean nativeHookActive;

    private HotkeyManager(){}

    public static void initDefaults() {

        uploadFromClipboard = HotkeyUtil.uploadFromClipboard();

    }

    public static Hotkey getUploadFromClipboardHotkey() {

        return uploadFromClipboard;

    }

    public static void setUploadFromClipboardHotkey(Hotkey hotkey) {

        uploadFromClipboard = hotkey;

    }

    /**
     * True only after {@code GlobalScreen.registerNativeHook()} succeeded.
     * On macOS this requires Accessibility permission for the running
     * process — when missing, hotkey recording silently captures nothing,
     * so the UI checks this flag to show a useful message instead.
     */
    public static boolean isNativeHookActive() {

        return nativeHookActive;

    }

    public static void setNativeHookActive(boolean active) {

        nativeHookActive = active;

    }

}
