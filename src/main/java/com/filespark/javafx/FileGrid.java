// a resizeable grid of file tiles

package com.filespark.javafx;

import java.util.List;

import com.filespark.Config;
import com.filespark.files.RawFile;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

public class FileGrid extends ScrollPane {

    private final FlowPane grid = new FlowPane();

    public FileGrid(List<RawFile> files) {

        grid.setHgap(25);
        grid.setVgap(25);
        grid.setPadding(new Insets(25));
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-background-color: " + Config.mainBlack);

        for (RawFile file : files) {

            FileTile tile = new FileTile(file.getFileName(), file.getMediaType());
            grid.getChildren().add(tile); 

        }

        this.setContent(grid);
        this.setFitToWidth(true);
        this.setStyle("-fx-background-color: " + Config.mainBlack);

    }

    
}
