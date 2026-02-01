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
    private boolean isSelected = false;

    public UserPanel(User user) {

        MenuItem userSettings = new MenuItem("User Settings");
        MenuItem logOut = new MenuItem("Log Out");

        ContextMenu menu = new ContextMenu(userSettings, logOut);
        menu.getStyleClass().add("context-menu");
        userSettings.getStyleClass().add("menu-item");
        logOut.getStyleClass().add("menu-item");

        // @debug placeholder
        userSettings.setOnAction(event -> System.out.println("User Settings"));
        logOut.setOnAction(event -> System.out.println("Log Out"));

        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(10, 10, 0, 0));

        String name  = user != null && user.getName()  != null ? user.getName()  : defaultName;
        String email = user != null && user.getEmail() != null ? user.getEmail() : defaultEmail;

        ProfilePicture pic = new ProfilePicture(user != null ? user.getPicture() : null);

        Label nameLabel = new Label(name);
        nameLabel.setTextFill(Color.WHITE);

        Label emailLabel = new Label(email);
        emailLabel.setTextFill(Color.GRAY);
        emailLabel.setStyle("-fx-font-size: 12px;");

        VBox textBox = new VBox(nameLabel, emailLabel);
        textBox.setAlignment(Pos.CENTER_LEFT);

        getChildren().addAll(pic, textBox);

        setOnMousePressed(event -> {

            if (event.getButton() == javafx.scene.input.MouseButton.SECONDARY) {

                menu.show(this, event.getScreenX(), event.getScreenY());

            } 
            else {

                menu.hide();

            }

        });

        setOnMouseEntered(e -> setCursor(javafx.scene.Cursor.HAND));
        setOnMouseExited(e -> setCursor(javafx.scene.Cursor.DEFAULT));

        logOut.setOnAction(e -> { 

            AppSession.logout(); //@todo: make log out observable to re enable button
            AppStateManager.set(AppState.LOGGED_OUT);

        });

        addHoverEffect();

    }

    private void addHoverEffect(){

        setOnMouseEntered(e -> {

            setStyle("-fx-background-color: " + Config.mainGrey + ";");

        });

        setOnMouseExited(e -> {

            setStyle("-fx-background-color: transparent;");

        });

    }

    // @todo: stay grey when in settings menu
    public void setSelected(boolean value) {

        this.isSelected = value;

        if (this.isSelected) setStyle("-fx-background-color: #3a3737;");
        else setStyle("-fx-background-color: transparent;");

    }

}
