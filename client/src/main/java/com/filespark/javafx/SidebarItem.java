package com.filespark.javafx;

import com.filespark.Config;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class SidebarItem extends HBox {

    private boolean isSelected = false;
    private Button removeButton;
    private Runnable onRemove;

    public SidebarItem(String iconName, String text) {

        setSpacing(10);
        setPadding(new Insets(8, 14, 8, 14));
        setAlignment(Pos.CENTER_LEFT);
        setCursor(Cursor.HAND);

        Image icon = new Image(getClass().getResourceAsStream("/icons/" + iconName));
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(26);
        iconView.setFitHeight(20);

        Label label = new Label(text);
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-size: 18px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(iconView, label, spacer);

        addHoverEffect();

    }

    public void setOnRemove(Runnable onRemove) {

        this.onRemove = onRemove;

        if (removeButton == null) {

            removeButton = new Button("×");
            removeButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-padding: 0 6 2 6;" +
                "-fx-cursor: hand;" +
                "-fx-background-radius: 4;"
            );
            removeButton.setVisible(false);
            removeButton.setManaged(false);
            removeButton.setOnMouseEntered(e -> removeButton.setStyle(
                "-fx-background-color: rgba(239, 68, 68, 0.2);" +
                "-fx-text-fill: #ef4444;" +
                "-fx-font-size: 18px;" +
                "-fx-padding: 0 6 2 6;" +
                "-fx-cursor: hand;" +
                "-fx-background-radius: 4;"
            ));
            removeButton.setOnMouseExited(e -> removeButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-padding: 0 6 2 6;" +
                "-fx-cursor: hand;" +
                "-fx-background-radius: 4;"
            ));
            removeButton.setOnAction(e -> {
                if (this.onRemove != null) this.onRemove.run();
                e.consume();
            });
            removeButton.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED, javafx.scene.input.MouseEvent::consume);
            getChildren().add(removeButton);

        }

        boolean enable = onRemove != null;
        removeButton.setVisible(false);
        removeButton.setManaged(enable);

    }

    private void addHoverEffect() {

        setOnMouseEntered(e -> {

            if (!this.isSelected) setStyle("-fx-background-color: " + Config.mainGrey + ";");
            if (removeButton != null && removeButton.isManaged()) removeButton.setVisible(true);

        });

        setOnMouseExited(e -> {

            if (!this.isSelected) setStyle("-fx-background-color: transparent;");
            if (removeButton != null) removeButton.setVisible(false);

        });

    }

    public void setSelected(boolean value) {

        this.isSelected = value;

        if (this.isSelected) setStyle("-fx-background-color: #3a3737;");
        else setStyle("-fx-background-color: transparent;");

    }

}
