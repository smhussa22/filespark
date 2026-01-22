package com.filespark.javafx;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public final class BottomRightContainer {

    private final Stage stage;

    public BottomRightContainer(Stage owner) {

        stage = new Stage(StageStyle.UNDECORATED);
        stage.initOwner(owner);
        stage.setAlwaysOnTop(true);

        Region root = new Region();
        root.setPrefSize(320, 180);
        root.setStyle("-fx-background-color: white;");

        Scene scene = new Scene(root, Color.WHITE);

        stage.setScene(scene);
        stage.setResizable(false);

        positionBottomRight();

        stage.setOnCloseRequest(e -> e.consume());

    }

    private void positionBottomRight() {

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        stage.setX(bounds.getMaxX() - 320 - 20);
        stage.setY(bounds.getMaxY() - 180 - 20);

    }

    public void show() {

        stage.show();

    }

    public void hide() {

        stage.hide();

    }
    
}
