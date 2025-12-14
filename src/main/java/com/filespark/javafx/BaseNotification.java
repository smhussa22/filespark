package com.filespark.javafx;

import com.filespark.Config;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

public class BaseNotification extends HBox {

    protected final Label label;
    protected final ImageView iconView;
    protected final Button closeButton;

    public BaseNotification(String text, String iconName) {

        setSpacing(10);
        setPadding(new Insets(10, 14, 10, 14));
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("notification");

        Image icon = new Image(getClass().getResourceAsStream("/icons/" + iconName));

        iconView = new ImageView(icon);
        iconView.setFitWidth(20);
        iconView.setFitHeight(20);
        iconView.setPreserveRatio(true);

        label = new Label(text);
        label.setWrapText(true);
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: 600;");

        HBox.setHgrow(label, Priority.ALWAYS);

        // @todo: add a hover effect on this close button
        Image closeIcon = new Image(getClass().getResourceAsStream("/icons/cancelButton.png"));

        ImageView closeIconView = new ImageView(closeIcon);
        closeIconView.setFitWidth(10);
        closeIconView.setFitHeight(10);
        closeIconView.setPreserveRatio(true);
        closeIconView.setSmooth(true);
        closeIconView.setOpacity(0.65);

        closeButton = new Button();
        closeButton.setGraphic(closeIconView);
        closeButton.setFocusTraversable(false);
        closeButton.setPadding(Insets.EMPTY);

        closeButton.setMinSize(16, 16);
        closeButton.setPrefSize(16, 16);
        closeButton.setMaxSize(16, 16);

        closeButton.setStyle("-fx-background-color: transparent;" + "-fx-cursor: hand;");

        closeButton.setOnMouseEntered(e -> closeIconView.setOpacity(1.0));
        closeButton.setOnMouseExited(e -> closeIconView.setOpacity(0.65));
        setStyle(
        "-fx-background-color: " + Config.mainBlack + ";" +
        "-fx-background-radius: 10px;" +
        "-fx-border-color: " + Config.mainGrey + ";" +
        "-fx-border-width: 1px;" +
        "-fx-border-radius: 10px;"
        );

        DropShadow shadow = new DropShadow();
        shadow.setRadius(12);
        shadow.setOffsetY(3);
        shadow.setColor(Color.rgb(0, 0, 0, 0.45));
        setEffect(shadow);
        
        getChildren().addAll(iconView, label, closeButton);

    }

    public void setText(String text) {

        label.setText(text);

    }

    public void setIcon(String iconName) {

        iconView.setImage(new Image(getClass().getResourceAsStream("/icons/" + iconName)));

    }

    public Button getCloseButton() {

        return closeButton;

    }

}
