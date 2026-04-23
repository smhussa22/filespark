package com.filespark.javafx;

import java.util.List;

import com.filespark.Config;
import com.filespark.client.FastAPI.EvictionEntry;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public final class EvictionConfirmDialog {

    private EvictionConfirmDialog() {}

    public static boolean show(Window owner, String incomingFileName, long incomingBytes, long maxBytes, List<EvictionEntry> evict) {

        boolean[] result = { false };

        Stage dialog = new Stage(StageStyle.TRANSPARENT);
        if (owner != null) dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);

        Label titleLabel = new Label("Make room for upload?");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        String incomingDesc = (incomingFileName == null ? "This upload" : "\"" + incomingFileName + "\"")
            + " is " + formatBytes(incomingBytes)
            + ". Your storage limit is " + formatBytes(maxBytes) + ".";

        Label messageLabel = new Label(incomingDesc + " To make room, the following older files will be permanently deleted:");
        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #cfcfcf;");
        messageLabel.setMaxWidth(440);

        VBox list = new VBox(4);
        list.setPadding(new Insets(4, 0, 4, 0));
        long totalFreed = 0;
        for (EvictionEntry entry : evict) {

            String name = entry.name == null ? entry.id : entry.name;
            Label row = new Label("• " + name + "  (" + formatBytes(entry.sizeBytes) + ")");
            row.setWrapText(true);
            row.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");
            list.getChildren().add(row);
            totalFreed += Math.max(0, entry.sizeBytes);

        }

        ScrollPane scroll = new ScrollPane(list);
        scroll.setFitToWidth(true);
        scroll.setMaxHeight(220);
        scroll.setStyle("-fx-background: " + Config.mainBlack + "; -fx-background-color: " + Config.mainBlack + "; -fx-border-color: " + Config.mainGrey + "; -fx-border-radius: 6;");

        Label totalLabel = new Label(evict.size() + " file" + (evict.size() == 1 ? "" : "s") + " will be deleted, freeing " + formatBytes(totalFreed) + ".");
        totalLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #9aa0a6;");

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: " + Config.mainGrey + ";" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 6 14;" +
            "-fx-cursor: hand;"
        );

        Button confirmButton = new Button("Delete and upload");
        confirmButton.setStyle(
            "-fx-background-color: rgba(239, 68, 68, 0.15);" +
            "-fx-border-color: #ef4444;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-text-fill: #ef4444;" +
            "-fx-padding: 6 14;" +
            "-fx-cursor: hand;"
        );

        cancelButton.setOnAction(e -> {
            result[0] = false;
            dialog.close();
        });
        confirmButton.setOnAction(e -> {
            result[0] = true;
            dialog.close();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        HBox buttonRow = new HBox(8, spacer, cancelButton, confirmButton);
        buttonRow.setAlignment(Pos.CENTER_RIGHT);

        VBox card = new VBox(12, titleLabel, messageLabel, scroll, totalLabel, buttonRow);
        card.setPadding(new Insets(20));
        card.setStyle(
            "-fx-background-color: " + Config.mainBlack + ";" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: " + Config.mainGrey + ";" +
            "-fx-border-width: 1;"
        );
        card.setMaxWidth(500);

        StackPane root = new StackPane(card);
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.55);");
        root.setPadding(new Insets(40));

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        dialog.setScene(scene);

        if (owner != null) {
            dialog.setX(owner.getX());
            dialog.setY(owner.getY());
            dialog.setWidth(owner.getWidth());
            dialog.setHeight(owner.getHeight());
        }

        dialog.showAndWait();
        return result[0];

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
