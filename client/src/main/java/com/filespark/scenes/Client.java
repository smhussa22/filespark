package com.filespark.scenes;

import java.io.File;
import java.util.List;

import com.filespark.Config;
import com.filespark.client.User;
import com.filespark.files.ScanWindowsDownloads;
import com.filespark.javafx.FileGrid;
import com.filespark.javafx.Sidebar;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class Client extends StackPane {

    final User user;

    private final BorderPane mainLayout = new BorderPane();
    private final Node downloadsView;
    private LinkDashboard linkDashboardView;
    private final Node hotkeySettingsView;

    public Client(User user) {

        this.user = user;

        List<File> downloadedFiles = ScanWindowsDownloads.getDownloadsFiles(Config.filesPerFetch);
        downloadsView = new FileGrid(downloadedFiles);
        hotkeySettingsView = new HotkeySettings();

        Sidebar sidebar = new Sidebar(user, this::navigate);

        mainLayout.setLeft(sidebar);
        mainLayout.setCenter(downloadsView);

        getChildren().addAll(mainLayout);

    }

    private void navigate(Sidebar.Nav nav) {

        switch (nav) {
            case DOWNLOADS -> mainLayout.setCenter(downloadsView);
            case LINK_DASHBOARD -> {
                if (linkDashboardView == null) linkDashboardView = new LinkDashboard();
                else linkDashboardView.refresh();
                mainLayout.setCenter(linkDashboardView);
            }
            case CLIENT_SETTINGS -> mainLayout.setCenter(downloadsView); // no scene yet
            case HOTKEY_SETTINGS -> mainLayout.setCenter(hotkeySettingsView);
        }

    }

}
