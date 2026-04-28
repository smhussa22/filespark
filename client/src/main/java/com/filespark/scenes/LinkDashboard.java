package com.filespark.scenes;

import java.util.List;

import com.filespark.Config;
import com.filespark.client.FastAPI;
import com.filespark.client.LinkSummary;
import com.filespark.javafx.BaseNotification;
import com.filespark.javafx.ConfirmDialog;
import com.filespark.javafx.LinkTile;
import com.filespark.javafx.NotificationService;
import com.filespark.javafx.StorageBar;
import com.filespark.javafx.TipsBanner;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class LinkDashboard extends ScrollPane {

    private final VBox content;
    private final StorageBar storageBar;
    private final TipsBanner tipsBanner;

    public LinkDashboard() {

        content = new VBox(Config.space2);
        content.setPadding(new Insets(Config.space5, Config.space5, Config.space5, Config.space5));
        content.setFillWidth(true);

        storageBar = new StorageBar();
        tipsBanner = new TipsBanner();

        setContent(content);
        setFitToWidth(true);
        setHbarPolicy(ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        setPannable(true);

        setStyle("-fx-background: transparent; -fx-background-color: " + Config.bgBase + ";");

        refresh();

    }

    public void refresh() {

        content.getChildren().clear();
        storageBar.setLoading();
        content.getChildren().add(storageBar);
        content.getChildren().add(tipsBanner);
        content.getChildren().add(statusLabel("Loading your links..."));

        refreshUsage();

        Task<List<LinkSummary>> task = new Task<>() {
            @Override
            protected List<LinkSummary> call() throws Exception {
                return FastAPI.listMyFiles();
            }
        };

        task.setOnSucceeded(e -> Platform.runLater(() -> render(task.getValue())));
        task.setOnFailed(e -> Platform.runLater(() -> {
            content.getChildren().clear();
            content.getChildren().add(storageBar);
            content.getChildren().add(tipsBanner);
            Throwable ex = task.getException();
            content.getChildren().add(statusLabel("Failed to load links: " + (ex == null ? "unknown" : ex.getMessage())));
        }));

        Thread t = new Thread(task, "LinkDashboard-load");
        t.setDaemon(true);
        t.start();

    }

    private void refreshUsage() {

        Task<long[]> task = new Task<>() {
            @Override
            protected long[] call() throws Exception {
                return FastAPI.getStorageUsage();
            }
        };

        task.setOnSucceeded(e -> Platform.runLater(() -> {
            long[] usage = task.getValue();
            storageBar.setUsage(usage[0], usage[1]);
        }));
        task.setOnFailed(e -> Platform.runLater(() -> storageBar.setError("unavailable")));

        Thread t = new Thread(task, "LinkDashboard-usage");
        t.setDaemon(true);
        t.start();

    }

    private void render(List<LinkSummary> links) {

        content.getChildren().clear();
        content.getChildren().add(storageBar);
        content.getChildren().add(tipsBanner);

        if (links == null || links.isEmpty()) {
            content.getChildren().add(statusLabel("No uploads yet. Upload a file to see its link here."));
            return;
        }

        for (LinkSummary summary : links) {
            LinkTile[] tileRef = new LinkTile[1];
            LinkTile tile = new LinkTile(
                summary,
                () -> deleteTile(summary, tileRef[0]),
                visibility -> updateVisibility(summary, visibility)
            );
            tileRef[0] = tile;
            content.getChildren().add(tile);
        }

    }

    private void deleteTile(LinkSummary summary, Node tile) {

        if (summary == null || summary.id == null || summary.id.isBlank()) return;

        boolean confirmed = ConfirmDialog.show(
            getScene() != null ? getScene().getWindow() : null,
            "Delete upload?",
            "This will remove \"" + (summary.name == null ? summary.id : summary.name) + "\" permanently. This cannot be undone.",
            "Delete"
        );
        if (!confirmed) return;

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                FastAPI.deleteFile(summary.id);
                return null;
            }
        };

        task.setOnSucceeded(e -> Platform.runLater(() -> {
            content.getChildren().remove(tile);
            NotificationService.show(new BaseNotification("Link deleted", "success.png"));
            boolean noLinksLeft = content.getChildren().stream().noneMatch(n -> n instanceof LinkTile);
            if (noLinksLeft) {
                content.getChildren().add(statusLabel("No uploads yet. Upload a file to see its link here."));
            }
            refreshUsage();
        }));
        task.setOnFailed(e -> Platform.runLater(() -> {
            Throwable ex = task.getException();
            NotificationService.show(new BaseNotification("Delete failed: " + (ex == null ? "unknown" : ex.getMessage()), "error.png"));
        }));

        Thread t = new Thread(task, "LinkDashboard-delete");
        t.setDaemon(true);
        t.start();

    }

    private void updateVisibility(LinkSummary summary, String visibility) {

        if (summary == null || summary.id == null || summary.id.isBlank() || visibility == null) return;

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                FastAPI.setVisibility(summary.id, visibility);
                return null;
            }
        };

        task.setOnSucceeded(e -> Platform.runLater(() -> {
            summary.visibility = visibility;
            NotificationService.show(new BaseNotification("Visibility set to " + visibility, "success.png"));
        }));
        task.setOnFailed(e -> Platform.runLater(() -> {
            Throwable ex = task.getException();
            NotificationService.show(new BaseNotification("Visibility update failed: " + (ex == null ? "unknown" : ex.getMessage()), "error.png"));
        }));

        Thread t = new Thread(task, "LinkDashboard-visibility");
        t.setDaemon(true);
        t.start();

    }

    private Label statusLabel(String text) {

        Label label = new Label(text);
        label.setStyle(
            "-fx-text-fill: " + Config.textSecondary + ";" +
            "-fx-font-size: 13px;" +
            "-fx-padding: " + Config.space3 + " 0 0 " + Config.space2 + ";"
        );
        label.setAlignment(Pos.CENTER);
        return label;

    }

}
