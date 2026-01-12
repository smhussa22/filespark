package com.filespark.scenes;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HotkeySettings extends VBox {

    public HotkeySettings() {

        Label placeholder = new Label("Hotkey Settings (Placeholder)");
        placeholder.setStyle("-fx-font-size: 18px;");

        this.getChildren().add(placeholder);
        this.setSpacing(10);
        this.setStyle("-fx-padding: 20;");
    }
}
