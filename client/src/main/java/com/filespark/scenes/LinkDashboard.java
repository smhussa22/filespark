package com.filespark.scenes;

import java.io.File;

import com.filespark.javafx.LinkTile;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class LinkDashboard extends ScrollPane {

    private final VBox content;

    public LinkDashboard() {

        content = new VBox(10);
        content.setPadding(new Insets(16));
        content.setFillWidth(true);

        setContent(content);
        setFitToWidth(true);
        setHbarPolicy(ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        setPannable(true);

        setStyle("-fx-background: transparent;" + "-fx-background-color: transparent;");

        loadPlaceholders();

    }

    private void loadPlaceholders() {

        content.getChildren().addAll(
            new LinkTile(new File("prevUploadedFile.mp4")),
            new LinkTile(new File("lecture_recording_week_10_final_final.mp4")),
            new LinkTile(new File("screenshare_debug_session.mov")),
            new LinkTile(new File("filespark_demo_clip.webm")),
            new LinkTile(new File("very_long_filename_that_should_be_shortened_properly_by_the_tile_component.mp4"))
        );
    }

    public void addTile(LinkTile tile) {

        content.getChildren().add(tile);

    }

    public void clearTiles() {

        content.getChildren().clear();

    }
    
}
