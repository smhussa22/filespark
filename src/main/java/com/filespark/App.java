// the main for this app, everything runs from here, holds the primary scene for javafx

package com.filespark;

import java.io.File;

import com.filespark.files.ScanWindowsRecent;
import com.filespark.javafx.FileTile;
import com.filespark.files.ScanWindowsDownloads;

import javafx.scene.layout.StackPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

public class App extends Application {

    FileChooser fileChooser;

    public static void main(String[] args){

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        StackPane root = new StackPane();

        // placeholder for editing
        FileTile tile = new FileTile("document.pdf");

        root.getChildren().add(tile);
        root.setStyle("-fx-background-color: #1e1e1e");
        Scene scene = new Scene(root, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);

        primaryStage.setScene(scene);
        primaryStage.show();

    };

}
