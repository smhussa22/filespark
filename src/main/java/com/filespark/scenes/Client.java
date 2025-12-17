package com.filespark.scenes;

import java.io.File;
import java.util.List;

import com.filespark.Config;
import com.filespark.files.ScanWindowsDownloads;
import com.filespark.javafx.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class Client extends StackPane {

    public Client() {

        List<File> downloadedFiles = ScanWindowsDownloads.getDownloadsFiles(Config.filesPerFetch);

        Sidebar sidebar = new Sidebar();
        FileGrid fileGrid = new FileGrid(downloadedFiles);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setLeft(sidebar);
        mainLayout.setCenter(fileGrid);

        NotificationContainer notifications = new NotificationContainer();
        NotificationService.initialize(notifications);

        StackPane.setAlignment(notifications, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(notifications, new Insets(20));

        getChildren().addAll(mainLayout, notifications);

    }
}
