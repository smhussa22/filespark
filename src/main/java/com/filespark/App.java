// the main for this app, everything runs from here, holds the primary scene for javafx

package com.filespark;

import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.*;

import com.filespark.javafx.BaseNotification;
import com.filespark.javafx.FileGrid;
import com.filespark.javafx.Sidebar;
import com.filespark.files.ScanWindowsDownloads;
import com.filespark.javafx.NotificationContainer;
import com.filespark.javafx.NotificationService;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.control.Button;


import java.awt.*;

public class App extends Application {

    FileChooser fileChooser;

    public static void main(String[] args){

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        List<File> downloadedFiles = ScanWindowsDownloads.getDownloadsFiles(Config.filesPerFetch);

        Sidebar sidebar = new Sidebar();
        FileGrid fileGrid = new FileGrid(downloadedFiles);
        
        BorderPane mainLayout = new BorderPane();
        mainLayout.setLeft(sidebar);
        mainLayout.setCenter(fileGrid);

        mainLayout.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        StackPane.setAlignment(mainLayout, Pos.CENTER);

        StackPane root = new StackPane();
        root.getChildren().add(mainLayout);

        NotificationContainer notifications = new NotificationContainer();
        NotificationService.initialize(notifications);

        StackPane.setAlignment(notifications, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(notifications, new Insets(20));

        root.getChildren().add(notifications);

        Scene scene = new Scene(root, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
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
