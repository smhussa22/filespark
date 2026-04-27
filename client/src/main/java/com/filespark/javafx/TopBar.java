package com.filespark.javafx;

import com.filespark.Config;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class TopBar extends HBox {

    public TopBar() {

        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/icons/icon256.png")));
        logo.setFitWidth(24);
        logo.setFitHeight(24);
        logo.setPreserveRatio(true);
        logo.setSmooth(true);

        Label brandText = new Label("FileSpark");
        brandText.setTextFill(Color.web(Config.textPrimary));
        brandText.setStyle("-fx-font-size: 14px; -fx-font-weight: 600;");

        setSpacing(Config.space2);
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(Config.space3, Config.space4, Config.space3, Config.space4));
        setMinHeight(48);
        setPrefHeight(48);
        setStyle(
            "-fx-background-color: " + Config.bgSurface + ";" +
            "-fx-border-color: transparent transparent " + Config.borderSubtle + " transparent;" +
            "-fx-border-width: 0 0 1 0;"
        );

        getChildren().addAll(logo, brandText);

    }

}
