package com.filespark.javafx;


import com.filespark.Config;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SidebarSection extends VBox {

    public SidebarSection(String title, SidebarItem... items) {

        setSpacing(6);
        setPadding(new Insets(10, 0, 0, 0));

        Label header = new Label(title.toUpperCase());
        header.setStyle("-fx-font-size: 24px; -fx-text-fill: " + Config.mainOrange + "; -fx-font-weight: bold;");

        getChildren().add(header);
        getChildren().addAll(items);
    }

    public void addItem(SidebarItem item) {
        getChildren().add(item);
    }

}

