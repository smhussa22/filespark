package com.filespark.javafx;

import com.filespark.AppState;
import com.filespark.Config;
import com.filespark.client.AppSession;
import com.filespark.client.AppStateManager;
import com.filespark.client.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class UserPanel extends HBox {

    private static final String defaultName = "FileSpark User";
    private static final String defaultEmail = "filesparkuser@filespark.github.io";

    private static final String STYLE_BASE =
        "-fx-background-color: transparent;" +
        "-fx-background-radius: 8;" +
        "-fx-border-color: " + Config.borderSubtle + " transparent transparent transparent;" +
        "-fx-border-width: 1 0 0 0;";
    private static final String STYLE_HOVER =
        "-fx-background-color: " + Config.bgHover + ";" +
        "-fx-background-radius: 8;" +
        "-fx-border-color: " + Config.borderSubtle + " transparent transparent transparent;" +
        "-fx-border-width: 1 0 0 0;";

    public UserPanel(User user) {

        MenuItem logOut = new MenuItem("Log Out");
        ContextMenu menu = new ContextMenu(logOut);
        menu.getStyleClass().add("context-menu");
        logOut.getStyleClass().add("menu-item");

        logOut.setOnAction(e -> {
            AppSession.logout();
            AppStateManager.set(AppState.LOGGED_OUT);
        });

        setSpacing(Config.space3);
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(Config.space3, Config.space3, Config.space3, Config.space3));
        setStyle(STYLE_BASE);

        String name  = user != null && user.getName()  != null ? user.getName()  : defaultName;
        String email = user != null && user.getEmail() != null ? user.getEmail() : defaultEmail;

        ProfilePicture pic = new ProfilePicture(user != null ? user.getPicture() : null);

        Label nameLabel = new Label(name);
        nameLabel.setTextFill(Color.web(Config.textPrimary));
        nameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600;");

        Label emailLabel = new Label(email);
        emailLabel.setTextFill(Color.web(Config.textMuted));
        emailLabel.setStyle("-fx-font-size: 11px;");

        VBox textBox = new VBox(1, nameLabel, emailLabel);
        textBox.setAlignment(Pos.CENTER_LEFT);

        getChildren().addAll(pic, textBox);

        setOnMouseClicked(event -> {

            if (event.getButton() != javafx.scene.input.MouseButton.PRIMARY) return;
            if (menu.isShowing()) menu.hide();
            else menu.show(this, event.getScreenX(), event.getScreenY());

        });

        setOnMouseEntered(e -> {
            setCursor(javafx.scene.Cursor.HAND);
            setStyle(STYLE_HOVER);
        });
        setOnMouseExited(e -> {
            setCursor(javafx.scene.Cursor.DEFAULT);
            setStyle(STYLE_BASE);
        });

    }

}
