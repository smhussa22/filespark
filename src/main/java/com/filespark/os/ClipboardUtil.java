package com.filespark.os;

import javafx.application.Platform;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import java.io.File;

public final class ClipboardUtil {

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

    public static File getFileFromClipboard() { 

        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (!clipboard.hasFiles()) return null;
        File file = clipboard.getFiles().get(0);
        return file;

    }

}
