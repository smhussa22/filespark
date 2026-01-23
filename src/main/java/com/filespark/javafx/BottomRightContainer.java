package com.filespark.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.filespark.Config;

public final class BottomRightContainer {

    private final Stage stage;
    private final NotificationContainer container;

    public BottomRightContainer(Stage owner) {

        stage = new Stage(StageStyle.TRANSPARENT);
        stage.initOwner(owner);
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);

        container = new NotificationContainer();
        container.setMaxWidth(Config.BOTTOM_RIGHT_CONTAINER_WIDTH - 20);
        container.setMouseTransparent(false);

        StackPane root = new StackPane(container);
        root.setPadding(new Insets(10));
        root.setPrefSize(Config.BOTTOM_RIGHT_CONTAINER_WIDTH, Config.BOTTOM_RIGHT_CONTAINER_HEIGHT);
        root.setStyle("""
            -fx-background-color: transparent;
            -fx-border-color: rgba(0, 255, 0, 0.9);
            -fx-border-width: 2;
            -fx-border-radius: 8;
        """);

        Scene scene = new Scene(root, Config.BOTTOM_RIGHT_CONTAINER_WIDTH, Config.BOTTOM_RIGHT_CONTAINER_HEIGHT);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);

        positionBottomRight();
        stage.setOnCloseRequest(e -> e.consume());

        NotificationService.initialize(container);
    }

    private void positionBottomRight() {

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setX(bounds.getMaxX() - Config.BOTTOM_RIGHT_CONTAINER_WIDTH - 20);
        stage.setY(bounds.getMaxY() - Config.BOTTOM_RIGHT_CONTAINER_HEIGHT - 20);

    }

    public void show() {

        stage.show();

    }

    public void hide() {

        stage.hide();

    }

}
