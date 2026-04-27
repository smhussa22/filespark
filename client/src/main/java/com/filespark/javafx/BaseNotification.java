package com.filespark.javafx;

import com.filespark.Config;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class BaseNotification extends HBox {

    public enum Kind { INFO, SUCCESS, ERROR, WARNING }

    private static final double WIDTH = 360;
    private static final double STRIPE_WIDTH = 3;
    private static final double ICON_BOX = 28;
    private static final double CLOSE_BOX = 22;
    private static final int DEFAULT_TIMEOUT_MS = 5000;

    protected final Label label;
    protected final ImageView iconView;
    protected final Button closeButton;
    private final PauseTransition autoDismiss;

    public BaseNotification(String text, String iconName) {

        this(text, iconName, kindFromIcon(iconName));

    }

    public BaseNotification(String text, String iconName, Kind kind) {

        setSpacing(0);
        setPadding(Insets.EMPTY);
        setAlignment(Pos.TOP_LEFT);
        setMinHeight(56);
        setPrefHeight(Region.USE_COMPUTED_SIZE);
        setMaxWidth(WIDTH);
        setPrefWidth(WIDTH);
        setMinWidth(WIDTH);
        setFillHeight(true);
        getStyleClass().add("notification");

        Region stripe = new Region();
        stripe.setMinWidth(STRIPE_WIDTH);
        stripe.setPrefWidth(STRIPE_WIDTH);
        stripe.setMaxWidth(STRIPE_WIDTH);
        stripe.setMinHeight(0);
        stripe.setMaxHeight(Double.MAX_VALUE);
        stripe.setStyle(
            "-fx-background-color: " + accentColor(kind) + ";" +
            "-fx-background-radius: 12 0 0 12;"
        );

        Image icon = new Image(getClass().getResourceAsStream("/icons/" + iconName));
        iconView = new ImageView(icon);
        iconView.setFitWidth(18);
        iconView.setFitHeight(18);
        iconView.setPreserveRatio(true);
        iconView.setSmooth(true);

        StackPane iconWrap = new StackPane(iconView);
        iconWrap.setMinSize(ICON_BOX, ICON_BOX);
        iconWrap.setPrefSize(ICON_BOX, ICON_BOX);
        iconWrap.setMaxSize(ICON_BOX, ICON_BOX);
        iconWrap.setStyle(
            "-fx-background-color: " + Config.bgElevated + ";" +
            "-fx-background-radius: 6;"
        );

        // wrap icon in a column so it stays at top when text wraps to multiple lines
        VBox iconCol = new VBox(iconWrap);
        iconCol.setAlignment(Pos.TOP_LEFT);

        label = new Label(text);
        label.setWrapText(true);
        label.setTextFill(Color.web(Config.textPrimary));
        label.setStyle("-fx-font-size: 13px; -fx-font-weight: 500;");
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMinHeight(ICON_BOX); // align baseline with icon for single-line case
        label.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(label, Priority.ALWAYS);

        Image closeIcon = new Image(getClass().getResourceAsStream("/icons/cancelButton.png"));
        ImageView closeIconView = new ImageView(closeIcon);
        closeIconView.setFitWidth(9);
        closeIconView.setFitHeight(9);
        closeIconView.setPreserveRatio(true);
        closeIconView.setSmooth(true);
        closeIconView.setOpacity(0.55);

        closeButton = new Button();
        closeButton.setGraphic(closeIconView);
        closeButton.setFocusTraversable(false);
        closeButton.setPadding(Insets.EMPTY);
        closeButton.setMinSize(CLOSE_BOX, CLOSE_BOX);
        closeButton.setPrefSize(CLOSE_BOX, CLOSE_BOX);
        closeButton.setMaxSize(CLOSE_BOX, CLOSE_BOX);
        closeButton.setStyle(closeButtonStyle(false));
        closeButton.setOnMouseEntered(e -> {
            closeIconView.setOpacity(1.0);
            closeButton.setStyle(closeButtonStyle(true));
        });
        closeButton.setOnMouseExited(e -> {
            closeIconView.setOpacity(0.55);
            closeButton.setStyle(closeButtonStyle(false));
        });

        // close button anchored to top so its position is uniform regardless of label wrap
        VBox closeCol = new VBox(closeButton);
        closeCol.setAlignment(Pos.TOP_RIGHT);
        closeCol.setMinWidth(CLOSE_BOX);

        HBox body = new HBox(Config.space3, iconCol, label, closeCol);
        body.setAlignment(Pos.TOP_LEFT);
        body.setPadding(new Insets(Config.space3, Config.space3, Config.space3, Config.space3));
        body.setFillHeight(true);
        HBox.setHgrow(body, Priority.ALWAYS);

        setStyle(
            "-fx-background-color: " + Config.bgSurface + ";" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + Config.borderSubtle + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 12;"
        );

        DropShadow shadow = new DropShadow();
        shadow.setRadius(18);
        shadow.setOffsetY(4);
        shadow.setColor(Color.rgb(0, 0, 0, 0.35));
        setEffect(shadow);

        getChildren().addAll(stripe, body);

        autoDismiss = new PauseTransition(Duration.millis(DEFAULT_TIMEOUT_MS));
        autoDismiss.setOnFinished(e -> closeButton.fire());

        // pause auto-dismiss while user hovers
        setOnMouseEntered(e -> autoDismiss.pause());
        setOnMouseExited(e -> autoDismiss.playFromStart());

    }

    public void startAutoDismiss() {

        if (autoDismiss != null) autoDismiss.playFromStart();

    }

    public void cancelAutoDismiss() {

        if (autoDismiss != null) autoDismiss.stop();

    }

    private static String accentColor(Kind kind) {
        return switch (kind) {
            case SUCCESS -> Config.success;
            case ERROR -> Config.danger;
            case WARNING -> Config.warning;
            case INFO -> Config.accent;
        };
    }

    private static Kind kindFromIcon(String iconName) {
        if (iconName == null) return Kind.INFO;
        String n = iconName.toLowerCase();
        if (n.contains("success")) return Kind.SUCCESS;
        if (n.contains("error") || n.contains("fail")) return Kind.ERROR;
        if (n.contains("warn")) return Kind.WARNING;
        return Kind.INFO;
    }

    private static String closeButtonStyle(boolean hover) {
        return "-fx-background-color: " + (hover ? Config.bgElevated : "transparent") + ";" +
               "-fx-background-radius: 4;" +
               "-fx-cursor: hand;";
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
