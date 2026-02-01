package com.filespark.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class TopBar extends HBox {
    
    public TopBar() {

        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/icons/icon256.png")));
        logo.setFitWidth(44);
        logo.setFitHeight(44);
        logo.setPreserveRatio(true);

        Label brandText = new Label("FileSpark");
        brandText.setStyle("-fx-text-fill: white;" + "-fx-font-size: 24px;" + "-fx-font-weight: bold;");

        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(20, 24, 12, 24));

        getChildren().addAll(logo, brandText);

    }

}
