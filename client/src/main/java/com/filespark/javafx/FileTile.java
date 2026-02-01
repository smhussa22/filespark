// a part of the file grid displaying a singular file along with its preview

package com.filespark.javafx;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.filespark.Config;
import com.filespark.client.UploadManager;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class FileTile extends StackPane {

    public FileTile(File file) {

        String fileName = file.getName();

        MenuItem uploadItem = new MenuItem("Upload As Embedded Link");
        MenuItem showItem = new MenuItem("Show In Folder");

        ContextMenu menu = new ContextMenu(uploadItem, showItem);
        menu.getStyleClass().add("context-menu");
        uploadItem.getStyleClass().add("menu-item");
        showItem.getStyleClass().add("menu-item");

        uploadItem.setOnAction(event -> { UploadManager.startUpload(file); });
        showItem.setOnAction(event -> System.out.println("Show In Folder: " + fileName)); //@debug placeholder

        ImageView imageView = new ImageView();
        Image preview = getPreview(file, imageView);
        if (preview != null) {

            imageView.setImage(preview);
            imageView.setFitHeight(Config.fileTileHeight);
            imageView.setFitWidth(Config.fileTileWidth);
            imageView.setPreserveRatio(true);

        }
        else {

            imageView.setImage(getDefaultIcon(file));
            imageView.setFitWidth(64);
            imageView.setFitHeight(64);
            imageView.setOpacity(0.85);

        } 
        
        imageView.setSmooth(true);

        Rectangle clip = new Rectangle(Config.fileTileWidth, Config.fileTileHeight);

        clip.setArcWidth(12);
        clip.setArcHeight(12);

        if (preview != null) imageView.setClip(clip);

        Rectangle border = new Rectangle(Config.fileTileWidth, Config.fileTileHeight);

        border.setFill(Color.web(Config.mainBlack));    
        border.setStroke(Color.web(Config.mainGrey)); 
        border.setStrokeWidth(1);
        border.setArcWidth(8);
        border.setArcHeight(8);

        StackPane imageHolder = new StackPane(border, imageView);
        Label label = new Label(shortenFileName(fileName));

        label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        label.setTextFill(Color.WHITE);

        VBox layout = new VBox(6, imageHolder, label);
        
        layout.setAlignment(Pos.CENTER);
        getChildren().add(layout);

        layout.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {

            if (event.getButton() == MouseButton.SECONDARY) {

                menu.show(this, event.getScreenX(), event.getScreenY());

            }
            else {

                menu.hide();

            }

        });

        layout.setOnMouseEntered(event -> {

            layout.setCursor(javafx.scene.Cursor.HAND);

        });

        layout.setOnMouseExited(event -> {

            layout.setCursor(javafx.scene.Cursor.DEFAULT);

        });

    }

    private String shortenFileName(String fileName) {

        if (fileName.length() <= Config.fileTileNameLength) return fileName;
        
        int dotExtension = fileName.lastIndexOf('.');
        if (dotExtension <= 0) return fileName.substring(0, Config.fileTileNameLength - 3) + "...";

        String name = fileName.substring(0, dotExtension);
        String extension = fileName.substring(dotExtension);

        int allowedNameLength = Config.fileTileNameLength - extension.length() - 3;
        if (allowedNameLength <= 0) {

            return "..." + extension.substring(extension.length(), (Config.fileTileNameLength - 3));

        }

        return name.substring (0, allowedNameLength) + "..." + extension;

    }

    private Image getPreview(File file, ImageView imageView) {

        String mimeType = null;

        try {

            mimeType = Files.probeContentType(file.toPath());

        } 
        catch (IOException ignore) {}

        if (mimeType == null) return null;

        if (mimeType.startsWith("image/")) {

            Image preview = new Image(file.toURI().toString(), Config.fileTileWidth, Config.fileTileHeight, true, true, true);

            preview.errorProperty().addListener((obs, old, error) -> {

                if (error) {

                    imageView.setImage(getDefaultIcon(file));

                }

            });

            return preview;

        }

        //@todo these
        if (mimeType.startsWith("video/")) return null;
        if (mimeType.startsWith("audio/")) return null;
        if (mimeType.startsWith("application/")) return null;

        return null;

    }

    private Image getDefaultIcon(File file) {

        String path = getMimeType(file);
        return new Image(getClass().getResourceAsStream(path));

    }

    private String getMimeType(File file) {

        try {

            String mimeType = Files.probeContentType(file.toPath());

            if (mimeType == null) return "/icons/default.png";
            if (mimeType.startsWith("image/")) return "/icons/image.png";
            if (mimeType.startsWith("video/")) return "/icons/video.png";
            if (mimeType.startsWith("application/")) return "/icons/application.png";
            if (mimeType.startsWith("audio/")) return "/icons/audio.png";

        } 
        catch (IOException ignore) {}

        return "/icons/default.png";

    }

    
}
