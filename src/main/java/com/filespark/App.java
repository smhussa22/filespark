// the main for this app, everything runs from here, holds the primary scene for javafx

package com.filespark;

import com.filespark.client.AppSession;
import com.filespark.client.AppStateManager;
import com.filespark.client.User;
import com.filespark.scenes.Authenticating;
import com.filespark.scenes.Client;
import com.filespark.scenes.Login;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;

public class App extends Application {

    private Client clientScene;
    private final Login logInScene = new Login();
    private final Authenticating authScene = new Authenticating();
    private Stage primaryStage;

    public static void main(String[] args){

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        AppStateManager.set(AppState.LOGGED_OUT);

        StackPane root = new StackPane(logInScene, authScene);

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

        AppStateManager.property().addListener((obs, oldState, newState) -> {

            if (newState == AppState.LOGGED_IN) {

                onLoginSuccess();

            }

            render();

        });

        render();
        primaryStage.show();

    }

    // @todo: move these out of app
    private void render() {

            logInScene.setVisible(false);
            authScene.setVisible(false);
            if (clientScene != null) clientScene.setVisible(false);

            switch (AppStateManager.get()) {
                case LOGGED_OUT -> logInScene.setVisible(true);
                case AUTHENTICATING -> authScene.setVisible(true);
                case LOGGED_IN -> {
                    
                    if (clientScene != null) {

                        clientScene.setVisible(true);

                    }

                }

            }


            System.out.println("Rendered state → " + AppStateManager.get());
        }

        private void onLoginSuccess() {

            User user = AppSession.getUser().orElseThrow();
            clientScene = new Client(user);
            StackPane root = (StackPane) primaryStage.getScene().getRoot();
            root.getChildren().add(clientScene);

        }

}
