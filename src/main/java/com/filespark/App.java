package com.filespark;

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

    Button uploadFileButton;
    FileChooser fileChooser;

    public static void main(String[] args){

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle(Config.WINDOW_TITLE);
        uploadFileButton = new Button();
        uploadFileButton.setText("Upload File");
        
        uploadFileButton.setOnAction(this);
        StackPane layout = new StackPane();
        layout.getChildren().add(uploadFileButton);

        Scene scene = new Scene(layout, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
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

            if (file == null){

                System.out.println("No file selected");

            }

            try{

                RawFile rawFile = new RawFile(file);
                rawFile.printDebugInfo();
                S3Uploader.uploadFile(rawFile.getFile());

            }
            catch (Exception exception){

                System.err.println(exception.getMessage());

            }

        }

    }

}
