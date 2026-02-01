package com.filespark.scenes;

import com.filespark.Config;
import com.filespark.javafx.GoogleLoginButton;
import com.filespark.javafx.TopBar;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class Login extends VBox {

    public Login() {

        setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        setStyle("-fx-background-color: " + Config.mainBlack + ";");

        TopBar topBar = new TopBar();

        Label title = new Label("Welcome Back!");
        title.setStyle("-fx-text-fill: white;" + "-fx-font-size: 24px;" + "-fx-font-weight: bold;");

        Label subtitle = new Label("Sign in to access your files and manage your uploads.");
        subtitle.setStyle("-fx-text-fill: #9ca3af;" + "-fx-font-size: 13px;");

        GoogleLoginButton googleLoginButton = new GoogleLoginButton();

        Label footer = new Label("FileSpark currently only supports Google accounts");
        footer.setStyle("-fx-text-fill: #6b7280;" + "-fx-font-size: 11px;");

        VBox card = new VBox(14, title, subtitle, googleLoginButton, footer); 
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(32));
        card.setStyle("-fx-background-color: " + Config.mainGrey + ";" + "-fx-background-radius: 10;");

        card.setPrefWidth(380);
        card.setPrefHeight(260);
        card.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        StackPane centerWrapper = new StackPane(card);
        centerWrapper.setAlignment(Pos.CENTER);
        VBox.setVgrow(centerWrapper, Priority.ALWAYS);

        getChildren().addAll(topBar, centerWrapper);

    }

}
