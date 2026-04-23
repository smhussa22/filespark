package com.filespark.javafx;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Consumer;

import com.filespark.Config;
import com.filespark.client.LinkSummary;
import com.filespark.os.ClipboardUtil;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class LinkTile extends HBox {

    public LinkTile(LinkSummary summary, Runnable onDelete, Consumer<String> onVisibilityChange) {

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

        ImageView iconView = new ImageView(new Image(getClass().getResourceAsStream("/icons/copylink.png")));
        iconView.setFitWidth(56);
        iconView.setFitHeight(44);
        iconView.setPreserveRatio(true);

        String displayName = shortenFileName(summary.name == null ? "" : summary.name);
        Label nameLabel = new Label(displayName);
        nameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + Config.mainOrange);

        Label timeLabel = new Label(formatUploadTime(summary.createdAt));
        timeLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Config.altOrange);

        Label statsLabel = new Label(summary.viewCount + " views · " + summary.downloadCount + " downloads");
        statsLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");

        VBox textBox = new VBox(1, nameLabel, timeLabel, statsLabel);
        textBox.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ComboBox<String> visibilityBox = new ComboBox<>();
        visibilityBox.getItems().addAll("private", "unlisted", "public");
        visibilityBox.setValue(normalizeVisibility(summary.visibility));
        visibilityBox.setStyle("-fx-background-color: " + Config.mainBlack + "; -fx-text-fill: white;");
        visibilityBox.setTooltip(new javafx.scene.control.Tooltip("Visibility"));
        visibilityBox.valueProperty().addListener((obs, oldValue, newValue) -> {

            if (newValue == null || newValue.equals(oldValue)) return;
            if (onVisibilityChange != null) onVisibilityChange.accept(newValue);

        });

        Button copyButton = buildIconButton("copylink.png", "Copy link");
        copyButton.setOnAction(e -> {
            if (summary.viewUrl != null && !summary.viewUrl.isBlank()) {
                ClipboardUtil.copyToClipboard(summary.viewUrl);
                NotificationService.show(new BaseNotification("Link copied: " + summary.viewUrl, "success.png"));
            }
        });

        Button deleteButton = buildIconButton("cancelButton.png", "Delete");
        deleteButton.setOnAction(e -> {
            if (onDelete != null) onDelete.run();
        });

        getChildren().addAll(iconView, textBox, spacer, visibilityBox, copyButton, deleteButton);

    }

    private String normalizeVisibility(String visibility) {

        if (visibility == null) return "private";
        String lower = visibility.toLowerCase();
        if (lower.equals("public") || lower.equals("unlisted") || lower.equals("private")) return lower;
        return "private";

    }

    private Button buildIconButton(String iconName, String tooltip) {

        ImageView iv = new ImageView(new Image(getClass().getResourceAsStream("/icons/" + iconName)));
        iv.setFitWidth(22);
        iv.setFitHeight(22);
        iv.setPreserveRatio(true);

        Button button = new Button();
        button.setGraphic(iv);
        button.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 6;"
        );
        if (tooltip != null && !tooltip.isBlank()) {
            button.setTooltip(new javafx.scene.control.Tooltip(tooltip));
        }
        return button;

    }

    private String shortenFileName(String fileName) {

        if (fileName.length() <= Config.fileTileNameLength) return fileName;

        int dotExtension = fileName.lastIndexOf('.');
        if (dotExtension <= 0) return fileName.substring(0, Config.fileTileNameLength - 3) + "...";

        String name = fileName.substring(0, dotExtension);
        String extension = fileName.substring(dotExtension);

        int allowedNameLength = Config.fileTileNameLength - extension.length() - 3;
        if (allowedNameLength <= 0) return "..." + extension.substring(extension.length() - (Config.fileTileNameLength - 3));

        return name.substring(0, allowedNameLength) + "..." + extension;

    }

    private String formatUploadTime(String isoCreatedAt) {

        if (isoCreatedAt == null || isoCreatedAt.isBlank()) return "Uploaded recently";
        try {
            Instant instant = Instant.parse(isoCreatedAt);
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("MMMM dd, yyyy @ HH:mm")
                    .withZone(ZoneId.systemDefault());
            return "Uploaded on " + formatter.format(instant);
        } catch (DateTimeParseException e) {
            return "Uploaded recently";
        }

    }

}
