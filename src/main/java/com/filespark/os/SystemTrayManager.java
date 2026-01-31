package com.filespark.os;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import javafx.application.Platform;
import javafx.stage.Stage;

public class SystemTrayManager {

    private TrayIcon trayIcon;

    public void install(Stage primaryStage, Runnable onExit) { 

        if (!SystemTray.isSupported()) return;

        SystemTray systemTray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/icon256.png"));

        PopupMenu menu = new PopupMenu();
        MenuItem openItem = new MenuItem("Open main window");
        MenuItem exitItem = new MenuItem("Exit");

        openItem.addActionListener(e -> Platform.runLater(() -> {

            primaryStage.show();
            primaryStage.toFront();

        }));

        exitItem.addActionListener(e -> Platform.runLater(() -> {

            systemTray.remove(trayIcon);
            Platform.runLater(onExit);

        }));

        menu.add(openItem);
        menu.add(exitItem);

        trayIcon = new TrayIcon(image, "FileSpark", menu);
        trayIcon.setImageAutoSize(true);

        trayIcon.addActionListener(e -> Platform.runLater(() -> {

            primaryStage.show();
            primaryStage.toFront();

        }));

        try { 

            systemTray.add(trayIcon);

        }
        catch (AWTException exception) {

            exception.getMessage();

        }

    }

    public void remove() {

        if (trayIcon != null) {

            SystemTray.getSystemTray().remove(trayIcon);

        }

    }
    
}
