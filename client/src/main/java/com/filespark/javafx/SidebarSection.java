package com.filespark.javafx;


import com.filespark.Config;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class SidebarSection extends VBox {

    private final HBox headerBox;

    public SidebarSection(String title, SidebarItem... items) {

        setSpacing(2);
        setPadding(new Insets(Config.space3, 0, 0, 0));

        Label header = new Label(title.toUpperCase());
        header.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + Config.textMuted + ";"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        headerBox = new HBox(Config.space2, header, spacer);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(Config.space2, Config.space3, Config.space2, Config.space3));

        getChildren().add(headerBox);
        getChildren().addAll(items);
    }

    public void setHeaderAction(Node action) {

        if (action == null) return;
        headerBox.getChildren().add(action);

    }

    public void addItem(SidebarItem item) {
        getChildren().add(item);
    }

}
