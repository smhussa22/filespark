// the main for this app, everything runs from here, holds the primary scene for javafx

package com.filespark;

import java.awt.Color;
import java.io.File;
import java.util.*;
import java.util.stream.*;

import com.filespark.files.ScanWindowsRecent;
import com.filespark.javafx.FileTile;
import com.filespark.files.ScanWindowsDownloads;
import com.filespark.files.RawFile;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

public class App extends Application {

    FileChooser fileChooser;

    public static void main(String[] args){

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        List<File> downloadedFiles = ScanWindowsDownloads.getDownloadsFiles(Config.filesPerFetch);
        List<RawFile> rawFiles = downloadedFiles.stream().map(file -> {

            try { return new RawFile(file); } 
            catch (Exception e) { return null; }
        
        }).filter(file -> file != null).collect(Collectors.toList());

        FlowPane grid = new FlowPane();
        grid.setHgap(25);
        grid.setVgap(25);
        grid.setPadding(new Insets(25));
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setStyle("-fx-background-color: " + Config.mainBlack);

        for (RawFile file : rawFiles) {

            FileTile tile = new FileTile(file.getFileName(), file.getMediaType());
            grid.getChildren().add(tile); 

        }

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: " + Config.mainBlack);

        Scene scene = new Scene(scroll, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);

        primaryStage.setScene(scene);
        primaryStage.setTitle("FileSpark");
        primaryStage.show();

    };

}
