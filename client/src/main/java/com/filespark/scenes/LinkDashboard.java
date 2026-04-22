package com.filespark.scenes;

import java.util.List;

import com.filespark.Config;
import com.filespark.client.FastAPI;
import com.filespark.client.LinkSummary;
import com.filespark.javafx.BaseNotification;
import com.filespark.javafx.LinkTile;
import com.filespark.javafx.NotificationService;

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

    public LinkDashboard() {

        content = new VBox(10);
        content.setPadding(new Insets(16));
        content.setFillWidth(true);

        setContent(content);
        setFitToWidth(true);
        setHbarPolicy(ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        setPannable(true);

        setStyle("-fx-background: transparent; -fx-background-color: " + Config.mainBlack + ";");

        refresh();

    }

    public void refresh() {

        content.getChildren().clear();
        content.getChildren().add(statusLabel("Loading your links..."));

        Task<List<LinkSummary>> task = new Task<>() {
            @Override
            protected List<LinkSummary> call() throws Exception {
                return FastAPI.listMyFiles();
            }
        };

        task.setOnSucceeded(e -> Platform.runLater(() -> render(task.getValue())));
        task.setOnFailed(e -> Platform.runLater(() -> {
            content.getChildren().clear();
            Throwable ex = task.getException();
            content.getChildren().add(statusLabel("Failed to load links: " + (ex == null ? "unknown" : ex.getMessage())));
        }));

        Thread t = new Thread(task, "LinkDashboard-load");
        t.setDaemon(true);
        t.start();

    }

    private void render(List<LinkSummary> links) {

        content.getChildren().clear();
        if (links == null || links.isEmpty()) {
            content.getChildren().add(statusLabel("No uploads yet. Upload a file to see its link here."));
            return;
        }

        for (LinkSummary summary : links) {
            LinkTile[] tileRef = new LinkTile[1];
            LinkTile tile = new LinkTile(summary, () -> deleteTile(summary, tileRef[0]));
            tileRef[0] = tile;
            content.getChildren().add(tile);
        }

    }

    private void deleteTile(LinkSummary summary, Node tile) {

        if (summary == null || summary.id == null || summary.id.isBlank()) return;

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
            if (content.getChildren().isEmpty()) {
                content.getChildren().add(statusLabel("No uploads yet. Upload a file to see its link here."));
            }
        }));
        task.setOnFailed(e -> Platform.runLater(() -> {
            Throwable ex = task.getException();
            NotificationService.show(new BaseNotification("Delete failed: " + (ex == null ? "unknown" : ex.getMessage()), "error.png"));
        }));

        Thread t = new Thread(task, "LinkDashboard-delete");
        t.setDaemon(true);
        t.start();

    }

    private Label statusLabel(String text) {

        Label label = new Label(text);
        label.setStyle("-fx-text-fill: " + Config.altOrange + "; -fx-font-size: 14px;");
        label.setAlignment(Pos.CENTER);
        return label;

    }

}
