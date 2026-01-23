package com.filespark.os;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public class HotkeyManager {

    private static Hotkey uploadFromClipboard;
    
    private HotkeyManager(){}

    public static void initDefaults() {

        uploadFromClipboard = new Hotkey(NativeKeyEvent.CTRL_MASK | NativeKeyEvent.SHIFT_MASK, NativeKeyEvent.VC_U);
    
    }

    public static Hotkey getUploadFromClipboardHotkey() {

        return uploadFromClipboard;

    }

    public static void setUploadFromClipboardHotkey(Hotkey hotkey) {

        uploadFromClipboard = hotkey;

    }
    
}
