package com.filespark.os;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ClipboardUtil {

    private static final DateTimeFormatter STAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

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

        if (clipboard.hasFiles()) {

            File file = clipboard.getFiles().get(0);
            if (file != null && file.exists()) return file;

        }

        if (clipboard.hasImage()) {

            Image fxImage = clipboard.getImage();
            if (fxImage == null) return null;

            BufferedImage buffered = SwingFXUtils.fromFXImage(fxImage, null);
            if (buffered == null) return null;

            try {

                Path temp = Files.createTempFile("filespark-clip-" + LocalDateTime.now().format(STAMP) + "-", ".png");
                File out = temp.toFile();
                out.deleteOnExit();
                ImageIO.write(buffered, "png", out);
                return out;

            }
            catch (Exception exception) {

                System.err.println("Failed to write clipboard image: " + exception.getMessage());
                return null;

            }

        }

        return null;

    }

}
