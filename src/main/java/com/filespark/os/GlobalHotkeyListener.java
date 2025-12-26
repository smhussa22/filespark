package com.filespark.os;

import java.io.File;
import java.lang.annotation.Native;

import com.filespark.client.UploadManager;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javafx.application.Platform;

public class GlobalHotkeyListener implements NativeKeyListener {

    private final Hotkey hotkey = HotkeyUtil.uploadFromClipboard();
    
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {

        if (hotkey == null) return;

        int eventMods = e.getModifiers() & 0x0F; // @todo find a better fix than this
        int targetMask = hotkey.modifierMask();

        boolean modifiersMatch = (eventMods == targetMask);
        boolean keyMatch = e.getKeyCode() == hotkey.keyCode();

        if (modifiersMatch && keyMatch) {

            Platform.runLater(() -> {

                File file = ClipboardUtil.getFileFromClipboard();
                if (file != null) UploadManager.startUpload(file);
                else System.out.println("No file exists on the clipboard."); // @todo: throw notification

            });

        }

    }

}
