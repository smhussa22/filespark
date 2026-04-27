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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LinkTile extends HBox {

    private static final double BUTTON_HEIGHT = 28;
    private static final double BUTTON_PAD_X = 12;

    private static final String STYLE_BASE =
        "-fx-background-color: " + Config.bgSurface + ";" +
        "-fx-background-radius: 10;" +
        "-fx-border-radius: 10;" +
        "-fx-border-color: " + Config.borderSubtle + ";" +
        "-fx-border-width: 1;";
    private static final String STYLE_HOVER =
        "-fx-background-color: " + Config.bgElevated + ";" +
        "-fx-background-radius: 10;" +
        "-fx-border-radius: 10;" +
        "-fx-border-color: " + Config.borderStrong + ";" +
        "-fx-border-width: 1;";

    public LinkTile(LinkSummary summary, Runnable onDelete, Consumer<String> onVisibilityChange) {

        setSpacing(Config.space3);
        setPadding(new Insets(Config.space3, Config.space4, Config.space3, Config.space3));
        setAlignment(Pos.CENTER_LEFT);
        setCursor(Cursor.DEFAULT);
        setStyle(STYLE_BASE);

        setOnMouseEntered(e -> setStyle(STYLE_HOVER));
        setOnMouseExited(e -> setStyle(STYLE_BASE));

        ImageView iconView = new ImageView(new Image(getClass().getResourceAsStream("/icons/copylink.png")));
        iconView.setFitWidth(20);
        iconView.setFitHeight(20);
        iconView.setPreserveRatio(true);
        iconView.setSmooth(true);

        StackPane iconWrap = new StackPane(iconView);
        iconWrap.setMinSize(40, 40);
        iconWrap.setPrefSize(40, 40);
        iconWrap.setMaxSize(40, 40);
        iconWrap.setStyle(
            "-fx-background-color: " + Config.bgElevated + ";" +
            "-fx-background-radius: 8;"
        );

        String displayName = shortenFileName(summary.name == null ? "" : summary.name);
        Label nameLabel = new Label(displayName);
        nameLabel.setTextFill(Color.web(Config.textPrimary));
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: 600;");

        Label timeLabel = new Label(formatUploadTime(summary.createdAt));
        timeLabel.setTextFill(Color.web(Config.textMuted));
        timeLabel.setStyle("-fx-font-size: 11px;");

        Label statsLabel = new Label(summary.viewCount + " views · " + summary.downloadCount + " downloads");
        statsLabel.setTextFill(Color.web(Config.textSecondary));
        statsLabel.setStyle("-fx-font-size: 11px;");

        VBox textBox = new VBox(2, nameLabel, statsLabel, timeLabel);
        textBox.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ComboBox<String> visibilityBox = new ComboBox<>();
        visibilityBox.getItems().addAll("private", "unlisted", "public");
        visibilityBox.setValue(normalizeVisibility(summary.visibility));
        visibilityBox.getStyleClass().add("fs-combo");
        visibilityBox.setPrefHeight(BUTTON_HEIGHT);
        visibilityBox.setMinHeight(BUTTON_HEIGHT);
        visibilityBox.setTooltip(new javafx.scene.control.Tooltip("Visibility"));
        visibilityBox.valueProperty().addListener((obs, oldValue, newValue) -> {

            if (newValue == null || newValue.equals(oldValue)) return;
            if (onVisibilityChange != null) onVisibilityChange.accept(newValue);

        });

        Button copyButton = textButton("Copy link");
        copyButton.setOnAction(e -> {
            String url = buildViewUrl(summary);
            if (url != null && !url.isBlank()) {
                ClipboardUtil.copyToClipboard(url);
                NotificationService.show(new BaseNotification("Link copied to clipboard", "success.png"));
            }
        });

        Button deleteButton = dangerButton("Delete");
        deleteButton.setOnAction(e -> {
            if (onDelete != null) onDelete.run();
        });

        getChildren().addAll(iconWrap, textBox, spacer, visibilityBox, copyButton, deleteButton);

    }

    private static String buildViewUrl(LinkSummary summary) {

        if (summary == null) return null;
        if (summary.ownerId != null && summary.id != null) {
            return Config.frontendDomain + "/f/" + summary.ownerId + "/" + summary.id;
        }
        return summary.viewUrl;

    }

    private String normalizeVisibility(String visibility) {

        if (visibility == null) return "private";
        String lower = visibility.toLowerCase();
        if (lower.equals("public") || lower.equals("unlisted") || lower.equals("private")) return lower;
        return "private";

    }

    private static Button textButton(String text) {

        Button b = new Button(text);
        b.setMinHeight(BUTTON_HEIGHT);
        b.setPrefHeight(BUTTON_HEIGHT);

        String base =
            "-fx-background-color: " + Config.bgElevated + ";" +
            "-fx-background-radius: 6;" +
            "-fx-border-color: " + Config.borderSubtle + ";" +
            "-fx-border-radius: 6;" +
            "-fx-border-width: 1;" +
            "-fx-text-fill: " + Config.textPrimary + ";" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 500;" +
            "-fx-padding: 0 " + BUTTON_PAD_X + ";" +
            "-fx-cursor: hand;";
        String hover =
            "-fx-background-color: " + Config.bgHover + ";" +
            "-fx-background-radius: 6;" +
            "-fx-border-color: " + Config.borderStrong + ";" +
            "-fx-border-radius: 6;" +
            "-fx-border-width: 1;" +
            "-fx-text-fill: " + Config.textPrimary + ";" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 500;" +
            "-fx-padding: 0 " + BUTTON_PAD_X + ";" +
            "-fx-cursor: hand;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(base));
        return b;

    }

    private static Button dangerButton(String text) {

        Button b = new Button(text);
        b.setMinHeight(BUTTON_HEIGHT);
        b.setPrefHeight(BUTTON_HEIGHT);

        String base =
            "-fx-background-color: " + Config.bgElevated + ";" +
            "-fx-background-radius: 6;" +
            "-fx-border-color: " + Config.borderSubtle + ";" +
            "-fx-border-radius: 6;" +
            "-fx-border-width: 1;" +
            "-fx-text-fill: " + Config.textSecondary + ";" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 500;" +
            "-fx-padding: 0 " + BUTTON_PAD_X + ";" +
            "-fx-cursor: hand;";
        String hover =
            "-fx-background-color: rgba(239,68,68,0.14);" +
            "-fx-background-radius: 6;" +
            "-fx-border-color: " + Config.danger + ";" +
            "-fx-border-radius: 6;" +
            "-fx-border-width: 1;" +
            "-fx-text-fill: " + Config.danger + ";" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 500;" +
            "-fx-padding: 0 " + BUTTON_PAD_X + ";" +
            "-fx-cursor: hand;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(base));
        return b;

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
                    .ofPattern("MMM d, yyyy · HH:mm")
                    .withZone(ZoneId.systemDefault());
            return formatter.format(instant);
        } catch (DateTimeParseException e) {
            return "Uploaded recently";
        }

    }

}
