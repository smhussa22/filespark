package com.filespark.javafx;

import com.filespark.Config;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public final class TypeToConfirmDialog {

    private TypeToConfirmDialog() {}

    public static boolean show(Window owner, String title, String message, String phrase, String confirmLabel) {

        boolean[] result = { false };

        Stage dialog = new Stage(StageStyle.TRANSPARENT);
        if (owner != null) dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #cfcfcf;");
        messageLabel.setMaxWidth(380);

        Label hintLabel = new Label("Type \"" + phrase + "\" to confirm:");
        hintLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #9aa0a6;");

        TextField input = new TextField();
        input.setPromptText(phrase);
        input.setStyle(
            "-fx-background-color: " + Config.mainBlack + ";" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: #6b7280;" +
            "-fx-border-color: " + Config.mainGrey + ";" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 6 10;"
        );

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

        Button confirmButton = new Button(confirmLabel == null ? "Confirm" : confirmLabel);
        String enabledStyle =
            "-fx-background-color: rgba(239, 68, 68, 0.18);" +
            "-fx-border-color: #ef4444;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-text-fill: #ef4444;" +
            "-fx-padding: 6 14;" +
            "-fx-cursor: hand;";
        String disabledStyle =
            "-fx-background-color: transparent;" +
            "-fx-border-color: " + Config.mainGrey + ";" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-text-fill: #6b7280;" +
            "-fx-padding: 6 14;" +
            "-fx-cursor: default;";

        confirmButton.setDisable(true);
        confirmButton.setStyle(disabledStyle);

        input.textProperty().addListener((obs, oldValue, newValue) -> {

            boolean matches = newValue != null && newValue.equals(phrase);
            confirmButton.setDisable(!matches);
            confirmButton.setStyle(matches ? enabledStyle : disabledStyle);

        });

        cancelButton.setOnAction(e -> {
            result[0] = false;
            dialog.close();
        });
        confirmButton.setOnAction(e -> {
            if (confirmButton.isDisabled()) return;
            result[0] = true;
            dialog.close();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        HBox buttonRow = new HBox(8, spacer, cancelButton, confirmButton);
        buttonRow.setAlignment(Pos.CENTER_RIGHT);

        VBox card = new VBox(12, titleLabel, messageLabel, hintLabel, input, buttonRow);
        card.setPadding(new Insets(20));
        card.setStyle(
            "-fx-background-color: " + Config.mainBlack + ";" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: " + Config.mainGrey + ";" +
            "-fx-border-width: 1;"
        );
        card.setMaxWidth(440);

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

}
