package com.filespark.scenes;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.filespark.Config;
import com.filespark.client.User;
import com.filespark.files.ScanWindowsDownloads;
import com.filespark.javafx.FileGrid;
import com.filespark.javafx.Sidebar;
import com.filespark.javafx.TopBar;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class Client extends StackPane {

    final User user;

    private final BorderPane mainLayout = new BorderPane();
    private final Node downloadsView;
    private LinkDashboard linkDashboardView;
    private final Node hotkeySettingsView;
    private final Node userSettingsView;
    private final Map<String, Node> directoryViews = new HashMap<>();

    public Client(User user) {

        this.user = user;

        List<File> downloadedFiles = ScanWindowsDownloads.getDownloadsFiles(Config.filesPerFetch);
        downloadsView = new FileGrid(downloadedFiles);
        hotkeySettingsView = new HotkeySettings();
        userSettingsView = new UserSettings(user);

        Sidebar sidebar = new Sidebar(user, this::navigate, this::showDirectory, this::forgetDirectory);

        mainLayout.setTop(new TopBar());
        mainLayout.setLeft(sidebar);
        mainLayout.setStyle("-fx-background-color: " + Config.bgBase + ";");

        setStyle("-fx-background-color: " + Config.bgBase + ";");
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
            case HOTKEY_SETTINGS -> mainLayout.setCenter(hotkeySettingsView);
            case USER_SETTINGS -> mainLayout.setCenter(userSettingsView);
        }

    }

    private void showDirectory(String label, File directory) {

        if (directory == null || !directory.isDirectory()) return;

        String key = directory.getAbsolutePath();
        Node view = directoryViews.computeIfAbsent(key, k -> new FileGrid(scanDirectory(directory)));
        mainLayout.setCenter(view);

    }

    private void forgetDirectory(File directory) {

        if (directory == null) return;
        Node removed = directoryViews.remove(directory.getAbsolutePath());
        if (removed != null && mainLayout.getCenter() == removed) {

            if (linkDashboardView == null) linkDashboardView = new LinkDashboard();
            else linkDashboardView.refresh();
            mainLayout.setCenter(linkDashboardView);

        }

    }

    private List<File> scanDirectory(File directory) {

        List<File> result = new ArrayList<>();
        File[] files = directory.listFiles(File::isFile);
        if (files == null) return result;

        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

        for (File file : files) {

            if (result.size() >= Config.filesPerFetch) break;

            String name = file.getName().toLowerCase();
            boolean allowed = Config.allowedExtensions.stream().anyMatch(name::endsWith);
            if (allowed) result.add(file);

        }
        return result;

    }

}
