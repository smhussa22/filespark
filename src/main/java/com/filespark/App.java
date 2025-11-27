// the main for this app, everything runs from here, holds the primary scene for javafx

package com.filespark;

import java.io.File;

import com.filespark.files.ScanWindowsRecent;

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

        // button for debugging and testing, will remove after
        Button testButton = new Button("Test Button");
        testButton.setOnAction(e -> {

            System.out.println("Scanning " + Config.filesPerFetch + " files...");
            for (File file : ScanWindowsRecent.getRecentFiles(Config.filesPerFetch)) {

                System.out.println(file.getName());
        
            }
            System.out.println("Scanning file sdone");

        });
        StackPane root = new StackPane();
        root.getChildren().add(testButton);
        Scene scene = new Scene(root, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();

    };

}
