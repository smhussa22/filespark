package com.filespark.scenes;

import com.filespark.Config;
import com.filespark.javafx.HotkeyTile;
import com.filespark.os.HotkeyManager;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HotkeySettings extends VBox {

    public HotkeySettings() {

        setSpacing(Config.space4);
        setPadding(new Insets(Config.space5));
        setStyle("-fx-background-color: " + Config.bgBase + ";");

        Label heading = new Label("Hotkey Settings");
        heading.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + Config.textPrimary + ";"
        );

        Label subtitle = new Label("Click a binding to record a new key combination.");
        subtitle.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + Config.textMuted + ";"
        );

        VBox header = new VBox(2, heading, subtitle);

        HotkeyTile uploadTile = new HotkeyTile("Upload Copied Item On Clipboard", HotkeyManager.getUploadFromClipboardHotkey(),
            newHotkey -> HotkeyManager.setUploadFromClipboardHotkey(newHotkey)
        );

        getChildren().addAll(header, uploadTile);

    }

}
