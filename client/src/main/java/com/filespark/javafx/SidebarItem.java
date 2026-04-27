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

    private static final String STYLE_BASE =
        "-fx-background-color: transparent;" +
        "-fx-background-radius: 8;";
    private static final String STYLE_HOVER =
        "-fx-background-color: " + Config.bgHover + ";" +
        "-fx-background-radius: 8;";
    private static final String STYLE_SELECTED =
        "-fx-background-color: " + Config.bgElevated + ";" +
        "-fx-background-radius: 8;";

    public SidebarItem(String iconName, String text) {

        setSpacing(Config.space3);
        setPadding(new Insets(Config.space2, Config.space3, Config.space2, Config.space3));
        setAlignment(Pos.CENTER_LEFT);
        setCursor(Cursor.HAND);
        setStyle(STYLE_BASE);

        Image icon = new Image(getClass().getResourceAsStream("/icons/" + iconName));
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(18);
        iconView.setFitHeight(18);
        iconView.setPreserveRatio(true);
        iconView.setSmooth(true);

        Label label = new Label(text);
        label.setTextFill(Color.web(Config.textPrimary));
        label.setStyle("-fx-font-size: 13px; -fx-font-weight: 500;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(iconView, label, spacer);

        addHoverEffect();

    }

    public void setOnRemove(Runnable onRemove) {

        this.onRemove = onRemove;

        if (removeButton == null) {

            removeButton = new Button("×");
            removeButton.setStyle(removeBtnStyle(false));
            removeButton.setVisible(false);
            removeButton.setManaged(false);
            removeButton.setMinSize(20, 20);
            removeButton.setPrefSize(20, 20);
            removeButton.setMaxSize(20, 20);
            removeButton.setOnMouseEntered(e -> removeButton.setStyle(removeBtnStyle(true)));
            removeButton.setOnMouseExited(e -> removeButton.setStyle(removeBtnStyle(false)));
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

    private static String removeBtnStyle(boolean hover) {
        return
            "-fx-background-color: " + (hover ? "rgba(239,68,68,0.18)" : "transparent") + ";" +
            "-fx-text-fill: " + (hover ? Config.danger : Config.textSecondary) + ";" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 0;" +
            "-fx-cursor: hand;" +
            "-fx-background-radius: 4;";
    }

    private void addHoverEffect() {

        setOnMouseEntered(e -> {

            if (!this.isSelected) setStyle(STYLE_HOVER);
            if (removeButton != null && removeButton.isManaged()) removeButton.setVisible(true);

        });

        setOnMouseExited(e -> {

            if (!this.isSelected) setStyle(STYLE_BASE);
            if (removeButton != null) removeButton.setVisible(false);

        });

    }

    public void setSelected(boolean value) {

        this.isSelected = value;
        setStyle(this.isSelected ? STYLE_SELECTED : STYLE_BASE);

    }

}
