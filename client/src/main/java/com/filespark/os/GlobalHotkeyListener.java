package com.filespark.os;

import java.io.File;

import com.filespark.client.UploadManager;
import com.filespark.javafx.BaseNotification;
import com.filespark.javafx.NotificationService;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javafx.application.Platform;

public class GlobalHotkeyListener implements NativeKeyListener {

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {

        Hotkey hotkey = HotkeyManager.getUploadFromClipboardHotkey();

        if (hotkey == null) return;

        int mods = HotkeyUtil.normalizeMask(e.getModifiers());

        if (mods == hotkey.modifierMask() && e.getKeyCode() == hotkey.keyCode()) {

            Platform.runLater(() -> {

                File file = ClipboardUtil.getFileFromClipboard();
                if (file != null) {
                    UploadManager.startUpload(file);
                } else {
                    NotificationService.show(new BaseNotification(
                        "No file on clipboard. Copy a file or screenshot first.",
                        "error.png"
                    ));
                }

            });

        }

    }

}
