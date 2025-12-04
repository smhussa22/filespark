// a part of the file grid displaying a singular file along with its preview

package com.filespark.javafx;

import com.filespark.Config;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FileTile extends StackPane {

    public FileTile(String fileName, String fileMimeType) {

        String path = getMimeType(fileName);

        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView imageView = new ImageView(image);

        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(false);

        Rectangle clip = new Rectangle(200, 150);

        clip.setArcWidth(12);
        clip.setArcHeight(12);

        imageView.setClip(clip);

        Rectangle border = new Rectangle(250, 150);

        border.setFill(Color.web(Config.mainBlack));    
        border.setStroke(Color.web(Config.mainGrey)); 
        border.setStrokeWidth(1);
        border.setArcWidth(12);
        border.setArcHeight(12);

        StackPane imageHolder = new StackPane(border, imageView);
        Label label = new Label(shortenFileName(fileName));

        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        label.setTextFill(Color.WHITE);

        VBox layout = new VBox(6, imageHolder, label);
        
        layout.setAlignment(Pos.CENTER);
        getChildren().add(layout);

    }

    private String shortenFileName(String fileName) {

        if (fileName.length() <= Config.fileTileNameLength) return fileName;
        return fileName.substring(0, Config.fileTileNameLength - 3) + "...";

    }

    private String getMimeType(String mimeType) {

        if (mimeType == null) return "/icons/default.png";
        mimeType = mimeType.toLowerCase();

        if (mimeType.startsWith("image/")) return "/icons/image.png";
        if (mimeType.startsWith("video/")) return "/icons/video.png";

        return "/icons/default.png";

    }
    
}
