package com.filespark.scenes;

import com.filespark.Config;
import com.filespark.javafx.GoogleLoginButton;
import com.filespark.javafx.TopBar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Authenticating extends VBox {

   

    public Authenticating() {

        setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        setStyle("-fx-background-color: " + Config.mainBlack + ";");

        TopBar topBar = new TopBar();

        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setPrefSize(40, 40);
        spinner.setStyle("-fx-progress-color: %s;".formatted(Config.mainOrange));

        Label title = new Label("Check your browser to log in!");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");

        Label subtitle = new Label("Didn't work? Try again:");
        subtitle.setStyle("-fx-text-fill: #b0b0b0; -fx-font-size: 13px;");

        GoogleLoginButton retryButton = new GoogleLoginButton();

        Label footer = new Label("FileSpark currently only supports Google accounts");
        footer.setStyle("-fx-text-fill: #6b7280;" + "-fx-font-size: 11px;");

        VBox card = new VBox(14, spinner, title, subtitle, retryButton, footer); 
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
