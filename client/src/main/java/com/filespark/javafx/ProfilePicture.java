package com.filespark.javafx;

import com.filespark.Config;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ProfilePicture extends StackPane {

    private static final String defaultAvatar = "/icons/user.png";

    public ProfilePicture(String imageUrl) {

        Image img;

        if (imageUrl == null || imageUrl.isBlank()) {

            img = new Image(getClass().getResourceAsStream(defaultAvatar));

        } 
        else if (imageUrl.startsWith("http")) {

            img = new Image(imageUrl, true);

        } 
        else {

            img = new Image(getClass().getResourceAsStream(imageUrl));

        }

        ImageView view = new ImageView(img);
        view.setFitWidth(40);
        view.setFitHeight(40);
        view.setPreserveRatio(false);

        Circle clip = new Circle(20, 20, 20);
        view.setClip(clip);

        Circle border = new Circle(20, 20, 20);
        border.setStroke(Color.web(Config.mainGrey));
        border.setStrokeWidth(1);
        border.setFill(Color.TRANSPARENT);

        getChildren().addAll(view, border);
        
    }
}
