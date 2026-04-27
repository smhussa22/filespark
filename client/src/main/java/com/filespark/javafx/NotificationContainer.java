package com.filespark.javafx;

import com.filespark.Config;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.util.Duration;


public class NotificationContainer extends VBox {

    public NotificationContainer() {

        setSpacing(Config.space2);
        setAlignment(Pos.BOTTOM_RIGHT);
        setPadding(new Insets(Config.space4, Config.space4, Config.space4, Config.space4));
        setPickOnBounds(false);
        setMaxWidth(380);
        setMinWidth(280);

    }

    public void show (BaseNotification notification){

        if(getChildren().size() >= Config.maxNotifications) {
            javafx.scene.Node oldest = getChildren().get(0);
            if (oldest instanceof BaseNotification bn) bn.cancelAutoDismiss();
            getChildren().remove(0);
        }

        notification.setTranslateX(60);
        notification.setOpacity(0);
        getChildren().add(notification);

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(220), notification);
        slideIn.setFromX(60);
        slideIn.setToX(0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(220), notification);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ParallelTransition in = new ParallelTransition(slideIn, fadeIn);
        in.setOnFinished(e -> notification.startAutoDismiss());
        in.play();

    }

    public void dismiss(BaseNotification notification){

        notification.cancelAutoDismiss();

        TranslateTransition slideOut = new TranslateTransition(Duration.millis(180), notification);
        slideOut.setFromX(0);
        slideOut.setToX(60);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(180), notification);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        ParallelTransition out = new ParallelTransition(slideOut, fadeOut);
        out.setOnFinished(e -> getChildren().remove(notification));
        out.play();

    }

}
