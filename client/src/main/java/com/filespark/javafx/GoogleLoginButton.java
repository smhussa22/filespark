package com.filespark.javafx;
import java.net.URI;

import com.filespark.AppState;
import com.filespark.Config;
import com.filespark.client.AppStateManager;
import com.filespark.client.BrowserLauncher;
import com.filespark.client.LoginCooldown;
import com.filespark.client.OAuthCallbackServer;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class GoogleLoginButton extends Button {

    private static final String DEFAULT_TEXT = "Continue With Google";

    private final Timeline cooldownTicker;

    public GoogleLoginButton() {

        ImageView googleLogo = new ImageView(new Image(getClass().getResourceAsStream("/icons/google_logo.png")));
        googleLogo.setFitHeight(24);
        googleLogo.setFitWidth(24);
        googleLogo.setPreserveRatio(true);

        setText(DEFAULT_TEXT);
        setPrefWidth(240);
        setPrefHeight(48);
        setGraphic(googleLogo);
        setContentDisplay(ContentDisplay.LEFT);
        setStyle("-fx-text-fill: #000000; -fx-font-size: 16px");

        cooldownTicker = new Timeline(new KeyFrame(Duration.seconds(1), e -> refreshCooldownUi()));
        cooldownTicker.setCycleCount(Animation.INDEFINITE);

        sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene == null) cooldownTicker.stop();
            else refreshCooldownUi();
        });

        setOnAction(e -> {

            if (LoginCooldown.isCoolingDown()) {

                refreshCooldownUi();
                return;

            }

            LoginCooldown.recordAttempt();

            try {

                AppStateManager.set(AppState.AUTHENTICATING);
                int port = OAuthCallbackServer.start();
                URI loginUri = new URI(Config.frontendDomain + "/desktop-login?port=" + port);
                boolean opened = BrowserLauncher.open(loginUri);
                if (!opened) {

                    NotificationService.show(new BaseNotification(
                        "Could not open browser. Visit " + loginUri + " manually.",
                        "error.png"
                    ));

                }

            }
            catch (Exception ignored) {}

            refreshCooldownUi();

        });

        refreshCooldownUi();

    }

    private void refreshCooldownUi() {

        long remainingMs = LoginCooldown.remainingMs();
        if (remainingMs <= 0L) {

            setDisable(false);
            setText(DEFAULT_TEXT);
            cooldownTicker.stop();
            return;

        }

        setDisable(true);
        setText("Try again in " + formatRemaining(remainingMs));
        if (cooldownTicker.getStatus() != Animation.Status.RUNNING) {

            cooldownTicker.playFromStart();

        }

    }

    private String formatRemaining(long ms) {

        long totalSeconds = (ms + 999L) / 1000L;
        if (totalSeconds < 60L) return totalSeconds + "s";
        long minutes = totalSeconds / 60L;
        long seconds = totalSeconds % 60L;
        if (seconds == 0L) return minutes + "m";
        return minutes + "m " + seconds + "s";

    }

}
