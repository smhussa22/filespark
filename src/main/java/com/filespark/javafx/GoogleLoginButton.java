package com.filespark.javafx;
import java.awt.Desktop;
import java.net.URI;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GoogleLoginButton extends Button {

    public GoogleLoginButton() {

        ImageView googleLogo = new ImageView(new Image(getClass().getResourceAsStream("/icons/google_logo.png")));
        googleLogo.setFitHeight(24);
        googleLogo.setFitWidth(24);
        googleLogo.setPreserveRatio(true);

        setText("Continue With Google");
        setPrefWidth(240);
        setPrefHeight(48);
        setGraphic(googleLogo);
        setContentDisplay(ContentDisplay.LEFT);
        setStyle("-fx-text-fill: #000000; -fx-font-size: 16px");

        setOnAction(e -> {

            try{

                Desktop.getDesktop().browse(new URI("http://localhost:8000/auth/google/login"));

            }
            catch(Exception ex){

                ex.printStackTrace();

            }

        });

    }
    
}
