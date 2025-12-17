// the main for this app, everything runs from here, holds the primary scene for javafx

package com.filespark;

import com.filespark.scenes.Client;
import com.filespark.scenes.Login;

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
    public void start(Stage primaryStage) {

        // temp
        final AppState[] state = { AppState.LOGGED_OUT };

        Scene scene = new Scene(new Login(), Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);

        primaryStage.setScene(scene);
        primaryStage.setTitle("FileSpark");
        primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/icons/icon256.png")));

        if (Taskbar.isTaskbarSupported()) {

            Taskbar taskbar = Taskbar.getTaskbar();

            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {

                taskbar.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/icon256.png")));

            }

        }

        // temp
        Runnable render = () -> {

            switch (state[0]) {

                case LOGGED_OUT, ERROR, AUTHENTICATING -> {

                    scene.setRoot(new Login());

                }

                case LOGGED_IN -> {

                    scene.setRoot(new Client());

                }

            }

            System.out.println("Rendered state → " + state[0]);

        };

        render.run();

        scene.setOnKeyPressed(event -> {

            switch (event.getCode()) {

                case L -> state[0] = AppState.LOGGED_OUT;
                case A -> state[0] = AppState.AUTHENTICATING;
                case I -> state[0] = AppState.LOGGED_IN;
                case E -> state[0] = AppState.ERROR;
                default -> { return; }

            }

            render.run();

        });

        primaryStage.show();

    }

}
