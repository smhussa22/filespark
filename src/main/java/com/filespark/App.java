package com.filespark;

import java.util.*;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class App extends Application implements EventHandler<ActionEvent> {

    // constants
    public static final String WINDOW_TITLE = "FileSpark Uploader Test";
    public static final double WINDOW_WIDTH = 750;
    public static final double WINDOW_HEIGHT = 750;
    // variables
    Button uploadFileButton;
    FileChooser fileChooser;

    public static void main(String[] args){

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle(WINDOW_TITLE);
        uploadFileButton = new Button();
        uploadFileButton.setText("Upload File");
        
        uploadFileButton.setOnAction(this);
        StackPane layout = new StackPane();
        layout.getChildren().add(uploadFileButton);

        Scene scene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();

    };

    @Override
    public void handle(ActionEvent event){

        if(event.getSource() == uploadFileButton){

            fileChooser = new FileChooser();
            fileChooser.setTitle("Select File");
            fileChooser.getExtensionFilters().addAll(

                new ExtensionFilter("All Files", "*.*"),
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new ExtensionFilter("Text Files", "*.txt", "*.csv"),
                new ExtensionFilter("PDF Files", "*.pdf")

            );

            File file = fileChooser.showOpenDialog(null);

            if (file != null){

                Path filePath = file.toPath();

                try {

                    String fileName = filePath.getFileName().toString();
                    double fileMegaBytes = (double) Files.size(filePath) / 1024 / 1024;
                    String mime = Files.probeContentType(filePath);

                    // debugging prints for the selected file
                    System.out.println("File Name: " + fileName);
                    System.out.println("Full Path: " + filePath.toAbsolutePath());
                    System.out.println("File Size: " + String.format("%.2f MB", fileMegaBytes));
                    System.out.println("MIME Type: " + (mime != null ? mime : "Unknown"));

                }
                catch (Exception exception) {

                    System.out.println(exception.getMessage());

                }
                
            }

        } else{

            System.out.println("No file has been selected");

        }

    }

}
