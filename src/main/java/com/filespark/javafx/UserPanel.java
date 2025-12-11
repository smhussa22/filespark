package com.filespark.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class UserPanel extends HBox {

    public UserPanel(String username, String email, ProfilePicture pic) {

        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(10, 10, 0 ,0));

        Label nameLabel = new Label(username);
        nameLabel.setTextFill(Color.WHITE);

        Label emailLabel = new Label(email);
        emailLabel.setTextFill(Color.GRAY);
        emailLabel.setStyle("-fx-font-size: 12px;");

        VBox textBox = new VBox(nameLabel, emailLabel);
        textBox.setAlignment(Pos.CENTER_LEFT);

        getChildren().addAll(pic, textBox);

    }


}
