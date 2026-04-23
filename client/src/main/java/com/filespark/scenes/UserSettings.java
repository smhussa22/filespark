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

        setSpacing(16);
        setPadding(new Insets(20));
        setStyle("-fx-background-color: " + Config.mainBlack + ";");

        Label heading = new Label("User Settings");
        heading.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + Config.mainOrange + ";");

        ProfilePicture pic = new ProfilePicture(user != null ? user.getPicture() : null);

        String name = user != null && user.getName() != null ? user.getName() : "FileSpark User";
        String email = user != null && user.getEmail() != null ? user.getEmail() : "";

        Label nameLabel = new Label(name);
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label emailLabel = new Label(email);
        emailLabel.setTextFill(Color.GRAY);
        emailLabel.setStyle("-fx-font-size: 13px;");

        VBox textBox = new VBox(2, nameLabel, emailLabel);
        textBox.setAlignment(Pos.CENTER_LEFT);

        HBox profileRow = new HBox(16, pic, textBox);
        profileRow.setAlignment(Pos.CENTER_LEFT);
        profileRow.setPadding(new Insets(14, 16, 14, 16));
        profileRow.setStyle(
            "-fx-background-color: " + Config.mainBlack + ";" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: " + Config.mainOrange + ";" +
            "-fx-border-width: 1.5;"
        );

        Label sectionLabel = new Label("Session");
        sectionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        Button logOutButton = new Button("Log Out");
        logOutButton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: " + Config.mainGrey + ";" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8 18;" +
            "-fx-cursor: hand;"
        );
        logOutButton.setOnAction(e -> {

            AppSession.logout();
            AppStateManager.set(AppState.LOGGED_OUT);

        });

        VBox sessionBox = new VBox(10, sectionLabel, logOutButton);
        sessionBox.setPadding(new Insets(14, 16, 14, 16));
        sessionBox.setStyle(
            "-fx-background-color: " + Config.mainBlack + ";" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: " + Config.mainGrey + ";" +
            "-fx-border-width: 1;"
        );

        Label dangerLabel = new Label("Danger Zone");
        dangerLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #ef4444; -fx-font-weight: bold;");

        Label dangerHint = new Label("Deletes your account and every file you've uploaded. This cannot be undone.");
        dangerHint.setWrapText(true);
        dangerHint.setStyle("-fx-font-size: 12px; -fx-text-fill: #cfcfcf;");

        Button deleteAccountButton = new Button("Delete Account");
        deleteAccountButton.setStyle(
            "-fx-background-color: rgba(239, 68, 68, 0.15);" +
            "-fx-border-color: #ef4444;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-text-fill: #ef4444;" +
            "-fx-padding: 8 18;" +
            "-fx-cursor: hand;"
        );
        deleteAccountButton.setOnAction(e -> handleDeleteAccount(deleteAccountButton));

        VBox dangerBox = new VBox(10, dangerLabel, dangerHint, deleteAccountButton);
        dangerBox.setPadding(new Insets(14, 16, 14, 16));
        dangerBox.setStyle(
            "-fx-background-color: " + Config.mainBlack + ";" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: #ef4444;" +
            "-fx-border-width: 1.5;"
        );

        getChildren().addAll(heading, profileRow, sessionBox, dangerBox);

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
