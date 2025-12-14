package com.filespark.javafx;

import com.filespark.Config;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.util.Duration;


public class NotificationContainer extends VBox {

    public NotificationContainer() {

        setSpacing(10);
        setAlignment(Pos.TOP_RIGHT);
        setPadding(new Insets(16, 16, 24, 16));
        setPickOnBounds(false);
        setMaxWidth(340);
        setMinWidth(250);
    }

    public void show (BaseNotification notification){

        if(getChildren().size() >= Config.maxNotifications) getChildren().remove(0);
        
        notification.setTranslateY(24);
        getChildren().add(notification);
        
        TranslateTransition slideUp = new TranslateTransition(Duration.millis(160), notification);
        slideUp.setFromY(24);
        slideUp.setToY(0);
        slideUp.play();

    }

    public void dismiss(BaseNotification notification){

        TranslateTransition slideOut = new TranslateTransition(Duration.millis(80), notification);
        slideOut.setFromX(0);
        slideOut.setToX(500);
        slideOut.setOnFinished(e -> getChildren().remove(notification));
        slideOut.play();

    }

}