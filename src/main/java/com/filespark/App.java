// the main for this app, everything runs from here, holds the primary scene for javafx

package com.filespark;

import java.io.File;
import java.util.*;
import java.util.stream.*;

import com.filespark.javafx.FileGrid;
import com.filespark.files.ScanWindowsDownloads;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.awt.*;

public class App extends Application {

    FileChooser fileChooser;

    public static void main(String[] args){

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        java.util.List<File> downloadedFiles = ScanWindowsDownloads.getDownloadsFiles(Config.filesPerFetch);

        FileGrid fileGrid = new FileGrid(downloadedFiles);
        Scene scene = new Scene(fileGrid, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/context-menu.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("FileSpark");
        primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/icons/icon256.png")));

        if (Taskbar.isTaskbarSupported()) {

            Taskbar taskbar = java.awt.Taskbar.getTaskbar();

            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {

                taskbar.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/icon256.png")));

            }
            
        }

        primaryStage.show();

    };

}
