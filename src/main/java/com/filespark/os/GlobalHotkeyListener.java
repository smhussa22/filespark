package com.filespark.os;

import java.io.File;

import com.filespark.client.UploadManager;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javafx.application.Platform;

public class GlobalHotkeyListener implements NativeKeyListener {

    //@debug constructor 
    public GlobalHotkeyListener() {
        System.out.println("========================================");
        System.out.println("GLOBAL HOTKEY LISTENER INIT");

        if (hotkey != null) {
            System.out.println("Hotkey modifier mask : " + hotkey.modifierMask());
            System.out.println("Hotkey key code      : " + hotkey.keyCode());
        } else {
            System.out.println("Hotkey is NULL");
        }

        System.out.println("========================================");
    }

    private final Hotkey hotkey = HotkeyUtil.uploadFromClipboard();
    
    @Override
    public void nativeKeyPressed(NativeKeyEvent e){

        // @debug
        System.out.println("========================================");
        System.out.println("NATIVE KEY EVENT");
        System.out.println("Key text        : " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        System.out.println("Key code        : " + e.getKeyCode());
        System.out.println("Event modifiers : " + e.getModifiers());

        if (hotkey == null){
            
            // @debug
            System.out.println("Hotkey          : NULL");
            System.out.println("========================================");
            return;

        }

        // @debug
        System.out.println("Expected key    : " + hotkey.keyCode());
        System.out.println("Expected mods   : " + hotkey.modifierMask());
        System.out.println("AND result      : " + (e.getModifiers() & hotkey.modifierMask()));

        boolean modifiersMatch = (e.getModifiers() & hotkey.modifierMask()) == hotkey.modifierMask();
        boolean keyMatch = e.getKeyCode() == hotkey.keyCode();

        // @debug
        System.out.println("Modifiers match : " + modifiersMatch);
        System.out.println("Key match       : " + keyMatch);

        if (modifiersMatch && keyMatch) {

            // @debug
            System.out.println(">>> HOTKEY TRIGGERED <<<");

            Platform.runLater(() -> {

                File file = ClipboardUtil.getFileFromClipboard();
                if (file != null){

                    // @debug
                    System.out.println("Clipboard file  : " + file.getAbsolutePath());
                    UploadManager.startUpload(file);

                }
                else { 
                
                    // @debug
                    System.err.println("No file exists on the clipboard");
                
                }

            });

        }

        // @debug
        System.out.println("========================================");

    }

}
