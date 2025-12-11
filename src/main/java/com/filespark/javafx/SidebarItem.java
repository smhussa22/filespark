package com.filespark.javafx;

import com.filespark.Config;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class SidebarItem extends HBox {

    private boolean isSelected = false;

    public SidebarItem(String iconName, String text) {

        setSpacing(10);
        setPadding(new Insets(8, 14, 8, 14));
        setAlignment(Pos.CENTER_LEFT);
        setCursor(Cursor.HAND);

        Image icon = new Image(getClass().getResourceAsStream("/icons/" + iconName));
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(26);
        iconView.setFitHeight(20);

        Label label = new Label(text);
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-size: 18px;");

        getChildren().addAll(iconView, label);

        addHoverEffect();

    }

    private void addHoverEffect() {

        setOnMouseEntered(e -> {

            if (!this.isSelected) setStyle("-fx-background-color: " + Config.mainGrey + ";");

        });

        setOnMouseExited(e -> {

            if (!this.isSelected) setStyle("-fx-background-color: transparent;");

        });

    }

    public void setSelected(boolean value) {

        this.isSelected = value;

        if (this.isSelected) setStyle("-fx-background-color: #3a3737;");
        else setStyle("-fx-background-color: transparent;");

    }

}
