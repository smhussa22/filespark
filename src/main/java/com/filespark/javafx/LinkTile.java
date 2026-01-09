package com.filespark.javafx;

import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.filespark.Config;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LinkTile extends HBox {

    public LinkTile(File file) {


        setSpacing(14);
        setPadding(new Insets(10, 16, 10, 16));
        setAlignment(Pos.CENTER_LEFT);
        setCursor(Cursor.HAND);
        setStyle(
            "-fx-background-color: " + Config.mainBlack + ";" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: " + Config.mainOrange + ";" +
            "-fx-border-width: 1.5;"
        );

        Image icon = new Image(getClass().getResourceAsStream("/icons/copylink.png"));
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(56);
        iconView.setFitHeight(44);
        iconView.setPreserveRatio(true);

        String fileName = shortenFileName(file.getName());
        Label nameLabel = new Label(fileName);
        nameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + Config.mainOrange);

        Label timeLabel = new Label(formatUploadTime(file));
        timeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + Config.altOrange);

        VBox textBox = new VBox(1);
        textBox.setAlignment(Pos.CENTER_LEFT);
        textBox.getChildren().addAll(nameLabel, timeLabel);

        getChildren().addAll(iconView, textBox);
    }

    // ---------- Helpers ----------

    private String shortenFileName(String fileName) {

        if (fileName.length() <= Config.fileTileNameLength) return fileName;

        int dotExtension = fileName.lastIndexOf('.');
        if (dotExtension <= 0)
            return fileName.substring(0, Config.fileTileNameLength - 3) + "...";

        String name = fileName.substring(0, dotExtension);
        String extension = fileName.substring(dotExtension);

        int allowedNameLength = Config.fileTileNameLength - extension.length() - 3;
        if (allowedNameLength <= 0) {
            return "..." + extension.substring(extension.length() - (Config.fileTileNameLength - 3));
        }

        return name.substring(0, allowedNameLength) + "..." + extension;
    }

    private String formatUploadTime(File file) {

        Instant instant = Instant.ofEpochMilli(file.lastModified());

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("MMMM dd, yyyy @ HH:mm")
                .withZone(ZoneId.systemDefault());

        return "Uploaded on " + formatter.format(instant);
    }
}
