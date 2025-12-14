package com.filespark.javafx;
import javafx.application.Platform;

public class NotificationService {

    private static NotificationContainer container;
    private NotificationService(){}

    public static void initialize(NotificationContainer notificationContainer){
        
        container = notificationContainer;

    }

    public static void show(BaseNotification notification) {

        if (container == null || notification == null) return;

        notification.getCloseButton().setOnAction(e -> Platform.runLater(() -> container.dismiss(notification)));
        Platform.runLater(() -> container.show(notification));
        
    }

}
