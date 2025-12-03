// a part of the file grid displaying a singular file along with its preview

package com.filespark.javafx;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FileTile extends StackPane {

    public FileTile(String fileName){

        String path = "/icons/default.png";
        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(false);
        Rectangle preview = new Rectangle(200, 200);
        preview.setFill(Color.web("#1e1e1e"));
        preview.setStroke(Color.web("#3d3d3d"));
        preview.setStrokeWidth(1);
        preview.setArcWidth(12);
        preview.setArcHeight(12);
        imageView.setClip(preview);
        Label label = new Label(fileName);
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        label.setTextFill(Color.WHITE);
        VBox layout = new VBox(12, preview, label);
        layout.setAlignment(Pos.CENTER);
        getChildren().add(layout);

    }
    
}
