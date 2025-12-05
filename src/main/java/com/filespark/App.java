// the main for this app, everything runs from here, holds the primary scene for javafx

package com.filespark;

import java.io.File;
import java.util.*;
import java.util.stream.*;

import com.filespark.javafx.FileGrid;
import com.filespark.javafx.FileTile;
import com.filespark.files.ScanWindowsDownloads;
import com.filespark.files.RawFile;

import javafx.scene.layout.FlowPane;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

        FileGrid fileGrid = new FileGrid(downloadedFiles);
        Scene scene = new Scene(fileGrid, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/context-menu.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("FileSpark");
        primaryStage.show();

    };

}
