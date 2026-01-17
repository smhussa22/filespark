package com.filespark.scenes;

import com.filespark.Config;
import com.filespark.javafx.HotkeyTile;
import com.filespark.os.Hotkey;
import com.filespark.client.HotkeyManager;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HotkeySettings extends VBox {

    public HotkeySettings() {

        setSpacing(12);
        setStyle("-fx-padding: 20; -fx-background-color: " + Config.mainBlack);

        HotkeyTile uploadTile = new HotkeyTile("Upload Copied Item On Clipboard", HotkeyManager.getUploadFromClipboardHotkey(),
        
            newHotkey -> {

                HotkeyManager.setUploadFromClipboardHotkey(newHotkey);

            }

        );

        getChildren().add(uploadTile);

    }

}
