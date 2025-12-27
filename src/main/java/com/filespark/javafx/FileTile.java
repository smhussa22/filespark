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
        String path = getMimeType(file);

        MenuItem uploadItem = new MenuItem("Upload As Embedded Link");
        MenuItem showItem = new MenuItem("Show In Folder");

        ContextMenu menu = new ContextMenu(uploadItem, showItem);
        menu.getStyleClass().add("context-menu");
        uploadItem.getStyleClass().add("menu-item");
        showItem.getStyleClass().add("menu-item");

        uploadItem.setOnAction(event -> { UploadManager.startUpload(file); });
        showItem.setOnAction(event -> System.out.println("Show In Folder: " + fileName)); //@debug placeholder

        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView imageView = new ImageView(image);

        imageView.setFitHeight(60);
        imageView.setFitWidth(60);
        imageView.setPreserveRatio(false);

        Rectangle clip = new Rectangle(145, 105);

        clip.setArcWidth(8);
        clip.setArcHeight(8);

        imageView.setClip(clip);

        Rectangle border = new Rectangle(175, 105);

        border.setFill(Color.web(Config.mainBlack));    
        border.setStroke(Color.web(Config.mainGrey)); 
        border.setStrokeWidth(1);
        border.setArcWidth(8);
        border.setArcHeight(8);

        StackPane imageHolder = new StackPane(border, imageView);
        Label label = new Label(shortenFileName(fileName));

        label.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
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

    private String getMimeType(File file) { // @todo: doesnt work

        try {

            String mimeType = Files.probeContentType(file.toPath());
            if (mimeType == null) return "/icons/default.png";
            if (mimeType.startsWith("image/")) return "/icons/image.png";
            if (mimeType.startsWith("video/")) return "/icons/video.png";
            if (mimeType.startsWith("application/")) return "/icons/application.png";
            if (mimeType.startsWith("audio/")) return "/icons/audio.png";

        }
        catch(IOException ignore) {}
        
        return "/icons/default.png";

    }

    
}
