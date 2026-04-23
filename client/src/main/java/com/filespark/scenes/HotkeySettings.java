package com.filespark.scenes;

import com.filespark.Config;
import com.filespark.javafx.HotkeyTile;
import com.filespark.os.HotkeyManager;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HotkeySettings extends VBox {

    public HotkeySettings() {

        setSpacing(16);
        setStyle("-fx-padding: 20; -fx-background-color: " + Config.mainBlack);

        Label heading = new Label("Hotkey Settings");
        heading.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + Config.mainOrange + ";");

        HotkeyTile uploadTile = new HotkeyTile("Upload Copied Item On Clipboard", HotkeyManager.getUploadFromClipboardHotkey(),

            newHotkey -> {

                HotkeyManager.setUploadFromClipboardHotkey(newHotkey);

            }

        );

        getChildren().addAll(heading, uploadTile);

    }

}
