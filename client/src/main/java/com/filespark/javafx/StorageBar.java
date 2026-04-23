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

public class StorageBar extends VBox {

    private final ProgressBar progressBar;
    private final Label usageLabel;

    public StorageBar() {

        setSpacing(8);
        setPadding(new Insets(14, 16, 14, 16));
        setStyle(
            "-fx-background-color: " + Config.mainBlack + ";" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: " + Config.mainOrange + ";" +
            "-fx-border-width: 1.5;"
        );

        Label title = new Label("Storage");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Config.mainOrange + ";");

        usageLabel = new Label("...");
        usageLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(8, title, spacer, usageLabel);
        header.setAlignment(Pos.CENTER_LEFT);

        progressBar = new ProgressBar(0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setPrefHeight(14);
        progressBar.setStyle("-fx-accent: " + Config.mainOrange + ";");

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
        if (ratio >= 0.9) accent = "#ef4444";
        else if (ratio >= 0.7) accent = "#eab308";
        else accent = Config.mainOrange;
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
