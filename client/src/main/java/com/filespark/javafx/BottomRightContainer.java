package com.filespark.javafx;

import javafx.collections.ListChangeListener;
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

    private static Stage ghostOwner;

    private final Stage stage;
    private final NotificationContainer container;

    public BottomRightContainer(Stage owner) {

        stage = new Stage(StageStyle.TRANSPARENT);
        stage.initOwner(getGhostOwner());
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);

        container = new NotificationContainer();
        container.setMaxWidth(Config.BOTTOM_RIGHT_CONTAINER_WIDTH - 20);
        container.setMouseTransparent(false);

        StackPane root = new StackPane(container);
        root.setPadding(new Insets(10));
        root.setPrefSize(Config.BOTTOM_RIGHT_CONTAINER_WIDTH, Config.BOTTOM_RIGHT_CONTAINER_HEIGHT);
        root.setStyle("-fx-background-color: transparent;");
        root.setPickOnBounds(false);

        Scene scene = new Scene(root, Config.BOTTOM_RIGHT_CONTAINER_WIDTH, Config.BOTTOM_RIGHT_CONTAINER_HEIGHT);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);

        positionBottomRight();
        stage.setOnCloseRequest(e -> e.consume());

        container.getChildren().addListener((ListChangeListener<javafx.scene.Node>) c -> {

            if (container.getChildren().isEmpty()) stage.hide();
            else if (!stage.isShowing()) {

                stage.show();
                stage.toFront();

            }

        });

        NotificationService.initialize(container);
    }

    private static Stage getGhostOwner() {

        if (ghostOwner == null) {

            ghostOwner = new Stage(StageStyle.UTILITY);
            ghostOwner.setOpacity(0);
            ghostOwner.setWidth(1);
            ghostOwner.setHeight(1);
            ghostOwner.setX(-20000);
            ghostOwner.setY(-20000);
            ghostOwner.show();

        }
        return ghostOwner;

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
