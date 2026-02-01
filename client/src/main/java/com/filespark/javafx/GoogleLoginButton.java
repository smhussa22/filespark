package com.filespark.javafx;
import java.awt.Desktop;
import java.net.URI;

import com.filespark.AppState;
import com.filespark.client.AppStateManager;
import com.filespark.client.OAuthCallbackServer;

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

            setDisable(true);

            try{

                AppStateManager.set(AppState.AUTHENTICATING);
                int port = OAuthCallbackServer.start();
                URI loginUri = new URI("http://localhost:8000/auth/google/login?port=" + port);
                // @todo
                Desktop.getDesktop().browse(loginUri);

            }
            catch(Exception ex){ // @todo: specify exception

                setDisable(false);
                ex.printStackTrace();

            }

        });

    }
    
}
