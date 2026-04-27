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
        setStyle("-fx-background-color: " + Config.bgBase + ";");

        TopBar topBar = new TopBar();

        Label title = new Label("Welcome back");
        title.setStyle(
            "-fx-text-fill: " + Config.textPrimary + ";" +
            "-fx-font-size: 22px;" +
            "-fx-font-weight: 600;"
        );

        Label subtitle = new Label("Sign in to access your files and manage your uploads.");
        subtitle.setStyle(
            "-fx-text-fill: " + Config.textSecondary + ";" +
            "-fx-font-size: 13px;"
        );

        GoogleLoginButton googleLoginButton = new GoogleLoginButton();

        Label footer = new Label("FileSpark currently only supports Google accounts");
        footer.setStyle(
            "-fx-text-fill: " + Config.textMuted + ";" +
            "-fx-font-size: 11px;"
        );

        VBox card = new VBox(Config.space4, title, subtitle, googleLoginButton, footer);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(Config.space6 + Config.space2));
        card.setStyle(
            "-fx-background-color: " + Config.bgSurface + ";" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + Config.borderSubtle + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 12;"
        );

        card.setPrefWidth(380);
        card.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        StackPane centerWrapper = new StackPane(card);
        centerWrapper.setAlignment(Pos.CENTER);
        VBox.setVgrow(centerWrapper, Priority.ALWAYS);

        getChildren().addAll(topBar, centerWrapper);

    }

}
