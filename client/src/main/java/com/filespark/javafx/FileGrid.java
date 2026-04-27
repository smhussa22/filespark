// a resizeable grid of file tiles

package com.filespark.javafx;

import java.util.List;
import java.io.File;

import com.filespark.Config;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class FileGrid extends ScrollPane {

    private final FlowPane grid = new FlowPane();

    public FileGrid(List<File> files) {

        grid.setHgap(Config.space5);
        grid.setVgap(Config.space5);
        grid.setPadding(new Insets(Config.space5));
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setStyle("-fx-background-color: " + Config.bgBase);

        for (File file : files) {

            FileTile[] tileRef = new FileTile[1];
            FileTile tile = new FileTile(file, () -> {
                grid.getChildren().remove(tileRef[0]);
                if (grid.getChildren().isEmpty()) showEmptyState();
            });
            tileRef[0] = tile;
            grid.getChildren().add(tile);

        }

        setFitToWidth(true);
        setStyle("-fx-background: transparent; -fx-background-color: " + Config.bgBase);

        if (files == null || files.isEmpty()) showEmptyState();
        else setContent(grid);

    }

    private void showEmptyState() {

        ImageView icon = new ImageView();
        try {
            icon.setImage(new Image(getClass().getResourceAsStream("/icons/default.png")));
            icon.setFitWidth(48);
            icon.setFitHeight(48);
            icon.setPreserveRatio(true);
            icon.setOpacity(0.4);
        } catch (Exception ignored) {}

        Label title = new Label("No suitable files in this folder");
        title.setTextFill(Color.web(Config.textPrimary));
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: 600;");

        Label hint = new Label("Supported formats: " + String.join(", ", Config.allowedExtensions));
        hint.setTextFill(Color.web(Config.textMuted));
        hint.setStyle("-fx-font-size: 11px;");
        hint.setWrapText(true);
        hint.setMaxWidth(420);
        hint.setAlignment(Pos.CENTER);

        VBox content = new VBox(Config.space2, icon, title, hint);
        content.setAlignment(Pos.CENTER);

        StackPane wrap = new StackPane(content);
        wrap.setStyle("-fx-background-color: " + Config.bgBase + ";");
        wrap.setPadding(new Insets(Config.space6));
        StackPane.setAlignment(content, Pos.CENTER);

        setContent(wrap);

    }

}
