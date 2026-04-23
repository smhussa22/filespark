package com.filespark.os;

public class HotkeyManager {

    private static Hotkey uploadFromClipboard;

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
    
}
