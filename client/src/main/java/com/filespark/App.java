// the main for this app, everything runs from here, holds the primary scene for javafx

package com.filespark;

import java.awt.Taskbar;
import java.awt.Toolkit;

import com.filespark.client.AppSession;
import com.filespark.client.AppStateManager;
import com.filespark.client.LoginCooldown;
import com.filespark.client.User;

import com.filespark.scenes.Authenticating;
import com.filespark.scenes.Client;
import com.filespark.scenes.Login;

import com.filespark.os.GlobalHotkeyListener;
import com.filespark.os.HotkeyManager;
import com.filespark.os.OperatingSystems;
import com.filespark.os.SystemTrayManager;

import com.filespark.javafx.BaseNotification;
import com.filespark.javafx.BottomRightContainer;
import com.filespark.javafx.NotificationService;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.application.Platform;

public class App extends Application {

    private static volatile javafx.application.HostServices HOST_SERVICES;

    public static javafx.application.HostServices hostServicesRef() {
        return HOST_SERVICES;
    }

    private Client clientScene;
    private final Login logInScene = new Login();
    private final Authenticating authScene = new Authenticating();
    private Stage primaryStage;
    private BottomRightContainer bottomRightContainer;
    private SystemTrayManager systemTrayManager;
    private boolean nativeHookRegistered;
    private volatile boolean shutdownRan;

    public static void main(String[] args) {

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) {

        HotkeyManager.initDefaults();

        try { // @Todo: make this only work when logged in

            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new GlobalHotkeyListener());
            nativeHookRegistered = true;
            HotkeyManager.setNativeHookActive(true);

        }
        catch (NativeHookException ex) {

            // Most common cause on macOS: the process running this JAR
            // doesn't have Accessibility permission, so jnativehook can't
            // capture global key events. Without registration, both the
            // default hotkey AND the recording UI in HotkeySettings stay
            // dead. Surface a notification once the UI is up so the user
            // knows what to fix instead of staring at a silent app.
            Platform.runLater(() -> NotificationService.show(new BaseNotification(
                hotkeyPermissionMessage(),
                "error.png"
            )));

        }

        this.primaryStage = primaryStage;
        HOST_SERVICES = getHostServices();
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
        primaryStage.setOnCloseRequest(e -> shutdown());

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
                LoginCooldown.reset();

            }
            render();

        });

        render();
        primaryStage.show();

        bottomRightContainer = new BottomRightContainer(primaryStage);

        systemTrayManager = new SystemTrayManager();
        systemTrayManager.install(primaryStage, this::shutdown);

    }

    @Override
    public void stop() {

        // JavaFX calls stop() on Cmd+Q (macOS app menu Quit), on the dock
        // Quit item, and on normal Platform.exit(). Without cleanup here
        // jnativehook's non-daemon thread keeps the JVM alive after the
        // window closes — macOS shows the app as "not responding".
        shutdown();

    }

    private synchronized void shutdown() {

        if (shutdownRan) return;
        shutdownRan = true;

        if (nativeHookRegistered) {
            try { GlobalScreen.unregisterNativeHook(); } catch (Exception ignored) {}
        }
        if (systemTrayManager != null) systemTrayManager.remove();
        Platform.exit();
        System.exit(0);

    }

    private static String hotkeyPermissionMessage() {

        if (OperatingSystem.OS == OperatingSystems.MAC) {
            return "Global hotkeys disabled. Grant FileSpark Accessibility permission in "
                 + "System Settings → Privacy & Security → Accessibility, then reopen the app.";
        }
        return "Global hotkeys could not be registered. Try running the app with elevated permissions.";

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
