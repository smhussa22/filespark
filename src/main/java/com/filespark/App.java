// the main for this app, everything runs from here, holds the primary scene for javafx

package com.filespark;

import com.filespark.scenes.Authenticating;
import com.filespark.scenes.Client;
import com.filespark.scenes.Login;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;


import java.awt.*;

public class App extends Application {
    
    private final Client clientScene = new Client();
    private final Login logInScene = new Login();
    private final Authenticating authScene = new Authenticating();

    public static void main(String[] args){

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) {

        // temp; will keep users logged in
        final AppState[] state = { AppState.LOGGED_OUT };

        StackPane root = new StackPane(logInScene, authScene, clientScene);

        Scene scene = new Scene(root, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/context-menu.css").toExternalForm());
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

            logInScene.setVisible(false);
            authScene.setVisible(false);
            clientScene.setVisible(false);

            switch (state[0]) {

                case LOGGED_OUT -> logInScene.setVisible(true);
                case AUTHENTICATING -> authScene.setVisible(true);
                case LOGGED_IN -> clientScene.setVisible(true);

            }

            System.out.println("Rendered state → " + state[0]);

        };

        render.run();

        scene.setOnKeyPressed(event -> {

            switch (event.getCode()) {

                case L -> state[0] = AppState.LOGGED_OUT;
                case A -> state[0] = AppState.AUTHENTICATING;
                case I -> state[0] = AppState.LOGGED_IN;
                default -> { return; }

            }

            render.run();

        });

        primaryStage.show();

    }

}
