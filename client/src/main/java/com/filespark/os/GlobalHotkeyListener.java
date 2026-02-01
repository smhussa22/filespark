package com.filespark.os;

import java.io.File;

import com.filespark.client.UploadManager;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javafx.application.Platform;

public class GlobalHotkeyListener implements NativeKeyListener {

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {

        Hotkey hotkey = HotkeyManager.getUploadFromClipboardHotkey();

        if (hotkey == null) return;

        int mods = e.getModifiers() & 0x0F;

        if (mods == hotkey.modifierMask() && e.getKeyCode() == hotkey.keyCode()) {

            Platform.runLater(() -> {

                File file = ClipboardUtil.getFileFromClipboard();
                if (file != null) UploadManager.startUpload(file);
                else System.out.println("No file exists on the clipboard.");

            });

        }

    }
    
}
