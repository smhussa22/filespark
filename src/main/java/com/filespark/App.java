// the main for this app, everything runs from here, holds the primary scene for javafx

package com.filespark;

import java.awt.Taskbar;
import java.awt.Toolkit;

import com.filespark.client.AppSession;
import com.filespark.client.AppStateManager;
import com.filespark.client.User;

import com.filespark.scenes.Authenticating;
import com.filespark.scenes.Client;
import com.filespark.scenes.Login;

import com.filespark.os.GlobalHotkeyListener;
import com.filespark.os.HotkeyManager;
import com.filespark.os.SystemTrayManager;

import com.filespark.javafx.BottomRightContainer;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.application.Platform;

public class App extends Application {

    private Client clientScene;
    private final Login logInScene = new Login();
    private final Authenticating authScene = new Authenticating();
    private Stage primaryStage;
    private BottomRightContainer bottomRightContainer;
    private SystemTrayManager systemTrayManager;

    public static void main(String[] args) {

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) {

        try { // @Todo: make this only work when logged in

            HotkeyManager.initDefaults();
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new GlobalHotkeyListener());
            
        } 
        catch (NativeHookException e) {

            System.err.println(e.getMessage());

        }

        this.primaryStage = primaryStage;
        AppStateManager.set(AppState.LOGGED_OUT);

        StackPane root = new StackPane(logInScene, authScene);

        Scene scene = new Scene(root, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/context-menu.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("FileSpark");
        primaryStage.getIcons().add(

            new javafx.scene.image.Image(

                getClass().getResourceAsStream("/icons/icon256.png")

            )

        );
        primaryStage.setOnCloseRequest(e -> {

            e.consume();
            primaryStage.hide();

        });

        if (Taskbar.isTaskbarSupported()) {

            Taskbar taskbar = Taskbar.getTaskbar();

            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {

                taskbar.setIconImage(

                    Toolkit.getDefaultToolkit().getImage(

                        getClass().getResource("/icons/icon256.png")

                    )

                );

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

        bottomRightContainer = new BottomRightContainer(primaryStage);
        bottomRightContainer.show();

        systemTrayManager = new SystemTrayManager();
        systemTrayManager.install(primaryStage, () -> {

            systemTrayManager.remove();
            Platform.exit();
            System.exit(0);

        });

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

    }

    private void onLoginSuccess() {
 
        User user = AppSession.getUser().orElseThrow();
        clientScene = new Client(user);
        StackPane root = (StackPane) primaryStage.getScene().getRoot();
        root.getChildren().add(clientScene);

    }
    
}
