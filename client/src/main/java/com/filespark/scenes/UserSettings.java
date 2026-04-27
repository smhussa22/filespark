package com.filespark.scenes;

import com.filespark.AppState;
import com.filespark.Config;
import com.filespark.client.AppSession;
import com.filespark.client.AppStateManager;
import com.filespark.client.FastAPI;
import com.filespark.client.User;
import com.filespark.javafx.BaseNotification;
import com.filespark.javafx.NotificationService;
import com.filespark.javafx.ProfilePicture;
import com.filespark.javafx.TypeToConfirmDialog;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class UserSettings extends VBox {

    public UserSettings(User user) {

        setSpacing(Config.space4);
        setPadding(new Insets(Config.space5));
        setStyle("-fx-background-color: " + Config.bgBase + ";");

        Label heading = new Label("User Settings");
        heading.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + Config.textPrimary + ";"
        );

        Label subtitle = new Label("Manage your account.");
        subtitle.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + Config.textMuted + ";"
        );

        VBox header = new VBox(2, heading, subtitle);

        ProfilePicture pic = new ProfilePicture(user != null ? user.getPicture() : null);

        String name = user != null && user.getName() != null ? user.getName() : "FileSpark User";
        String email = user != null && user.getEmail() != null ? user.getEmail() : "";

        Label nameLabel = new Label(name);
        nameLabel.setTextFill(Color.web(Config.textPrimary));
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: 600;");

        Label emailLabel = new Label(email);
        emailLabel.setTextFill(Color.web(Config.textMuted));
        emailLabel.setStyle("-fx-font-size: 12px;");

        VBox textBox = new VBox(2, nameLabel, emailLabel);
        textBox.setAlignment(Pos.CENTER_LEFT);

        HBox profileRow = new HBox(Config.space3, pic, textBox);
        profileRow.setAlignment(Pos.CENTER_LEFT);
        profileRow.setPadding(new Insets(Config.space3, Config.space4, Config.space3, Config.space4));
        profileRow.setStyle(
            "-fx-background-color: " + Config.bgSurface + ";" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: " + Config.borderSubtle + ";" +
            "-fx-border-width: 1;"
        );

        Button logOutButton = secondaryButton("Log Out");
        logOutButton.setOnAction(e -> {
            AppSession.logout();
            AppStateManager.set(AppState.LOGGED_OUT);
        });

        Button deleteAccountButton = dangerButton("Delete Account");
        deleteAccountButton.setOnAction(e -> handleDeleteAccount(deleteAccountButton));

        HBox actionsRow = new HBox(Config.space2, logOutButton, deleteAccountButton);
        actionsRow.setAlignment(Pos.CENTER_LEFT);

        getChildren().addAll(header, profileRow, actionsRow);

    }

    private static Button secondaryButton(String text) {

        Button b = new Button(text);
        String base =
            "-fx-background-color: " + Config.bgSurface + ";" +
            "-fx-text-fill: " + Config.textPrimary + ";" +
            "-fx-border-color: " + Config.borderSubtle + ";" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 6 14;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 500;" +
            "-fx-cursor: hand;";
        String hover =
            "-fx-background-color: " + Config.bgElevated + ";" +
            "-fx-text-fill: " + Config.textPrimary + ";" +
            "-fx-border-color: " + Config.borderStrong + ";" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 6 14;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 500;" +
            "-fx-cursor: hand;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(base));
        return b;

    }

    private static Button dangerButton(String text) {

        Button b = new Button(text);
        String base =
            "-fx-background-color: transparent;" +
            "-fx-text-fill: " + Config.danger + ";" +
            "-fx-border-color: rgba(239,68,68,0.4);" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 6 14;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 500;" +
            "-fx-cursor: hand;";
        String hover =
            "-fx-background-color: rgba(239,68,68,0.14);" +
            "-fx-text-fill: " + Config.danger + ";" +
            "-fx-border-color: " + Config.danger + ";" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 6 14;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 500;" +
            "-fx-cursor: hand;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(base));
        return b;

    }

    private void handleDeleteAccount(Button button) {

        boolean confirmed = TypeToConfirmDialog.show(
            getScene() != null ? getScene().getWindow() : null,
            "Delete account",
            "This will permanently delete your FileSpark account and every file you've uploaded.",
            "permanently delete",
            "Delete account"
        );
        if (!confirmed) return;

        button.setDisable(true);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                FastAPI.deleteAccount();
                return null;
            }
        };

        task.setOnSucceeded(e -> Platform.runLater(() -> {
            NotificationService.show(new BaseNotification("Account deleted.", "success.png"));
            AppSession.logout();
            AppStateManager.set(AppState.LOGGED_OUT);
        }));
        task.setOnFailed(e -> Platform.runLater(() -> {
            button.setDisable(false);
            Throwable ex = task.getException();
            NotificationService.show(new BaseNotification("Delete failed: " + (ex == null ? "unknown" : ex.getMessage()), "error.png"));
        }));

        Thread t = new Thread(task, "UserSettings-deleteAccount");
        t.setDaemon(true);
        t.start();

    }

}
