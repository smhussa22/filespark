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

        setSpacing(6);
        setPadding(new Insets(10, 0, 0, 0));

        Label header = new Label(title.toUpperCase());
        header.setStyle("-fx-font-size: 24px; -fx-text-fill: " + Config.mainOrange + "; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        headerBox = new HBox(8, header, spacer);
        headerBox.setAlignment(Pos.CENTER_LEFT);

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
