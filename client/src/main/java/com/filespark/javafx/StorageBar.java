package com.filespark.javafx;

import com.filespark.Config;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class StorageBar extends VBox {

    private final ProgressBar progressBar;
    private final Label usageLabel;

    public StorageBar() {

        setSpacing(Config.space2);
        setPadding(new Insets(Config.space3, Config.space4, Config.space3, Config.space4));
        setStyle(
            "-fx-background-color: " + Config.bgSurface + ";" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: " + Config.borderSubtle + ";" +
            "-fx-border-width: 1;"
        );

        Label title = new Label("Storage");
        title.setTextFill(Color.web(Config.textSecondary));
        title.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");

        usageLabel = new Label("...");
        usageLabel.setTextFill(Color.web(Config.textPrimary));
        usageLabel.setStyle("-fx-font-size: 12px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(Config.space2, title, spacer, usageLabel);
        header.setAlignment(Pos.CENTER_LEFT);

        progressBar = new ProgressBar(0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setPrefHeight(8);
        progressBar.getStyleClass().add("fs-progress-bar");
        progressBar.setStyle("-fx-accent: " + Config.accent + ";");

        getChildren().addAll(header, progressBar);

    }

    public void setLoading() {

        usageLabel.setText("Loading...");
        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);

    }

    public void setError(String message) {

        usageLabel.setText(message == null ? "unavailable" : message);
        progressBar.setProgress(0);

    }

    public void setUsage(long usedBytes, long maxBytes) {

        double ratio = maxBytes <= 0 ? 0 : Math.min(1.0, (double) usedBytes / (double) maxBytes);
        progressBar.setProgress(ratio);

        String accent;
        if (ratio >= 0.9) accent = Config.danger;
        else if (ratio >= 0.7) accent = Config.warning;
        else accent = Config.accent;
        progressBar.setStyle("-fx-accent: " + accent + ";");

        usageLabel.setText(formatBytes(usedBytes) + " / " + formatBytes(maxBytes) + "  ·  " + Math.round(ratio * 100) + "%");

    }

    private static String formatBytes(long bytes) {

        if (bytes < 1024) return bytes + " B";
        String[] units = { "KB", "MB", "GB", "TB" };
        double value = bytes / 1024.0;
        int unit = 0;
        while (value >= 1024 && unit < units.length - 1) {

            value /= 1024;
            unit++;

        }
        if (value >= 10) return String.format("%.0f %s", value, units[unit]);
        return String.format("%.1f %s", value, units[unit]);

    }

}
