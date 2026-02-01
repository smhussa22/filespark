package com.filespark.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;

public final class NotificationOverlay extends StackPane {

    private final NotificationContainer container;

    public NotificationOverlay() {

        setPickOnBounds(false);
        setMouseTransparent(false);

        container = new NotificationContainer();

        StackPane.setAlignment(container, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(container, new Insets(20));

        getChildren().add(container);

    }

    public void show(BaseNotification notification) {

        container.show(notification);

    }
    
}
