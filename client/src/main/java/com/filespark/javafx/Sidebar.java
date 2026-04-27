package com.filespark.javafx;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.filespark.Config;
import com.filespark.client.AppSession;
import com.filespark.client.FastAPI;
import com.filespark.client.User;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

public class Sidebar extends VBox {

    public enum Nav { DOWNLOADS, LINK_DASHBOARD, HOTKEY_SETTINGS, USER_SETTINGS }

    private SidebarItem selectedItem = null;
    private final SidebarSection browseSection;
    private final Consumer<Nav> onNavigate;
    private final BiConsumer<String, File> onShowDirectory;
    private final Consumer<File> onForgetDirectory;
    private final Map<String, SidebarItem> directoryItems = new LinkedHashMap<>();
    private SidebarItem downloadsItem;

    public Sidebar(
        User user,
        Consumer<Nav> onNavigate,
        BiConsumer<String, File> onShowDirectory,
        Consumer<File> onForgetDirectory
    ) {

        this.onNavigate = onNavigate;
        this.onShowDirectory = onShowDirectory;
        this.onForgetDirectory = onForgetDirectory;

        setPrefWidth(248);
        setMinWidth(248);
        setPadding(new Insets(Config.space3, Config.space2, Config.space3, Config.space2));
        setSpacing(Config.space2);

        setStyle(
            "-fx-background-color: " + Config.bgSurface + ";" +
            "-fx-border-color: transparent " + Config.borderSubtle + " transparent transparent;" +
            "-fx-border-width: 0 1 0 0;"
        );

        browseSection = new SidebarSection("Browse");
        browseSection.setHeaderAction(buildAddDirectoryButton());

        SidebarItem dashboard = item("sitem-default.png", "Link Dashboard", onNavigate, Nav.LINK_DASHBOARD);
        downloadsItem = item("sitem-downloads.png", "Downloads", onNavigate, Nav.DOWNLOADS);
        downloadsItem.setOnRemove(this::removeDownloadsEntry);
        browseSection.addItem(dashboard);

        boolean hideDownloads = user != null && user.isDownloadsHidden();
        if (!hideDownloads) browseSection.addItem(downloadsItem);

        if (user != null) {

            for (String path : user.getBrowseDirectories()) {

                if (path == null || path.isBlank()) continue;
                File dir = new File(path);
                if (!dir.isDirectory()) continue;
                addDirectoryItem(dir, false);

            }

        }

        SidebarSection settings = new SidebarSection("Settings");
        SidebarItem hotkey = item("sitem-default.png", "Hotkey Settings", onNavigate, Nav.HOTKEY_SETTINGS);
        SidebarItem userSettings = item("sitem-default.png", "User Settings", onNavigate, Nav.USER_SETTINGS);
        settings.addItem(hotkey);
        settings.addItem(userSettings);

        Region space = new Region();
        VBox.setVgrow(space, Priority.ALWAYS);

        UserPanel userPanel = new UserPanel(user);

        getChildren().addAll(browseSection, settings, space, userPanel);
        setSelect(dashboard);
        if (onNavigate != null) onNavigate.accept(Nav.LINK_DASHBOARD);

    }

    private Button buildAddDirectoryButton() {

        Button add = new Button("+");
        String base =
            "-fx-background-color: transparent;" +
            "-fx-text-fill: " + Config.textSecondary + ";" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 0;" +
            "-fx-cursor: hand;" +
            "-fx-background-radius: 4;";
        String hover =
            "-fx-background-color: " + Config.bgElevated + ";" +
            "-fx-text-fill: " + Config.textPrimary + ";" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 0;" +
            "-fx-cursor: hand;" +
            "-fx-background-radius: 4;";
        add.setMinSize(20, 20);
        add.setPrefSize(20, 20);
        add.setMaxSize(20, 20);
        add.setStyle(base);
        add.setOnMouseEntered(e -> add.setStyle(hover));
        add.setOnMouseExited(e -> add.setStyle(base));
        add.setTooltip(new javafx.scene.control.Tooltip("Add a directory to browse"));
        add.setOnAction(e -> pickAndAddDirectory());
        return add;

    }

    private void pickAndAddDirectory() {

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose a directory to browse");
        File picked = chooser.showDialog(getScene() != null ? getScene().getWindow() : null);
        if (picked == null || !picked.isDirectory()) return;

        if (directoryItems.containsKey(picked.getAbsolutePath())) {

            SidebarItem existing = directoryItems.get(picked.getAbsolutePath());
            setSelect(existing);
            if (onShowDirectory != null) onShowDirectory.accept(picked.getName(), picked);
            return;

        }

        SidebarItem entry = addDirectoryItem(picked, true);
        setSelect(entry);
        if (onShowDirectory != null) onShowDirectory.accept(picked.getName(), picked);

    }

    private SidebarItem addDirectoryItem(File directory, boolean persist) {

        SidebarItem entry = new SidebarItem("sitem-default.png", directory.getName());
        entry.setOnMouseClicked(e -> {
            setSelect(entry);
            if (onShowDirectory != null) onShowDirectory.accept(directory.getName(), directory);
        });
        entry.setOnRemove(() -> removeDirectoryEntry(entry, directory));

        directoryItems.put(directory.getAbsolutePath(), entry);
        browseSection.addItem(entry);

        if (persist) persistDirectories();

        return entry;

    }

    private void removeDownloadsEntry() {

        if (downloadsItem == null) return;
        boolean wasSelected = downloadsItem == selectedItem;
        browseSection.getChildren().remove(downloadsItem);

        AppSession.getUser().ifPresent(u -> u.setDownloadsHidden(true));

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                FastAPI.setDownloadsHidden(true);
                return null;
            }
        };
        task.setOnFailed(e -> Platform.runLater(() -> {
            Throwable ex = task.getException();
            NotificationService.show(new BaseNotification(
                "Could not save change: " + (ex == null ? "unknown" : ex.getMessage()),
                "error.png"
            ));
        }));
        Thread t = new Thread(task, "Sidebar-hideDownloads");
        t.setDaemon(true);
        t.start();

        if (wasSelected) {

            selectedItem = null;
            if (onNavigate != null) onNavigate.accept(Nav.LINK_DASHBOARD);

        }

    }

    private void removeDirectoryEntry(SidebarItem entry, File directory) {

        boolean wasSelected = entry == selectedItem;
        browseSection.getChildren().remove(entry);
        directoryItems.remove(directory.getAbsolutePath());

        persistDirectories();

        if (onForgetDirectory != null) onForgetDirectory.accept(directory);
        if (wasSelected) {

            selectedItem = null;
            if (onNavigate != null) onNavigate.accept(Nav.LINK_DASHBOARD);

        }

    }

    private void persistDirectories() {

        List<String> snapshot = new ArrayList<>(directoryItems.keySet());

        AppSession.getUser().ifPresent(u -> u.setBrowseDirectories(snapshot));

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                FastAPI.setBrowseDirectories(snapshot);
                return null;
            }
        };

        task.setOnFailed(e -> Platform.runLater(() -> {
            Throwable ex = task.getException();
            NotificationService.show(new BaseNotification(
                "Could not save directories: " + (ex == null ? "unknown" : ex.getMessage()),
                "error.png"
            ));
        }));

        Thread t = new Thread(task, "Sidebar-saveDirectories");
        t.setDaemon(true);
        t.start();

    }

    private SidebarItem item(String icon, String label, Consumer<Nav> onNavigate, Nav nav) {

        SidebarItem item = new SidebarItem(icon, label);
        item.setOnMouseClicked(e -> {
            setSelect(item);
            if (onNavigate != null) onNavigate.accept(nav);
        });
        return item;

    }

    private void setSelect(SidebarItem item) {

        if (selectedItem != null) selectedItem.setSelected(false);
        selectedItem = item;
        item.setSelected(true);

    }

}
