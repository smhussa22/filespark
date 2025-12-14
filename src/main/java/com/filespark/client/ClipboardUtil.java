package com.filespark.client;

import javafx.application.Platform;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class ClipboardUtil {

    private ClipboardUtil(){}
    
    public static void copyToClipboard(String text) {

        if (text == null || text.isEmpty()) return;
        Platform.runLater(() -> {

            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(text);
            clipboard.setContent(content);

        });
        
    }

}
