package com.filespark.javafx;

import com.filespark.Config;
import com.filespark.os.Hotkey;
import com.filespark.os.HotkeyManager;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class TipsBanner extends VBox {

    public TipsBanner() {

        setSpacing(Config.space2);
        setPadding(new Insets(Config.space3, Config.space4, Config.space3, Config.space4));
        setStyle(
            "-fx-background-color: " + Config.bgSurface + ";" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: " + Config.borderSubtle + ";" +
            "-fx-border-width: 1;"
        );

        Label heading = new Label("Quick start");
        heading.setTextFill(Color.web(Config.textSecondary));
        heading.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");

        getChildren().add(heading);
        getChildren().add(tipRow(formatHotkey(HotkeyManager.getUploadFromClipboardHotkey()),
            "Copy any file or screenshot, then press this hotkey anywhere — FileSpark uploads it and copies the share link to your clipboard."));
        getChildren().add(tipRow("Right-click",
            "On a file tile to upload, show in folder, or delete it from disk."));
        getChildren().add(tipRow("Sidebar +",
            "Add any folder to browse and upload from."));

    }

    private static HBox tipRow(String kbdText, String description) {

        Label kbd = new Label(kbdText);
        kbd.setTextFill(Color.web(Config.textPrimary));
        kbd.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-font-weight: 600;" +
            "-fx-padding: 3 8;" +
            "-fx-background-color: " + Config.bgElevated + ";" +
            "-fx-background-radius: 5;" +
            "-fx-border-color: " + Config.borderSubtle + ";" +
            "-fx-border-radius: 5;" +
            "-fx-border-width: 1;"
        );
        kbd.setMinWidth(Region.USE_PREF_SIZE);

        StackPane kbdWrap = new StackPane(kbd);
        kbdWrap.setMinWidth(120);
        kbdWrap.setAlignment(Pos.CENTER_LEFT);

        Label dot = new Label("·");
        dot.setTextFill(Color.web(Config.textMuted));

        Text desc = new Text(description);
        desc.setFill(Color.web(Config.textSecondary));
        desc.setStyle("-fx-font-size: 12px;");
        TextFlow descFlow = new TextFlow(desc);
        HBox.setHgrow(descFlow, Priority.ALWAYS);

        HBox row = new HBox(Config.space3, kbdWrap, descFlow);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;

    }

    @SuppressWarnings("unused")
    private static Circle bullet() {
        Circle c = new Circle(2);
        c.setFill(Color.web(Config.accent));
        return c;
    }

    private static String formatHotkey(Hotkey hotkey) {

        if (hotkey == null) return "Unassigned";

        int mask = hotkey.modifierMask();
        StringBuilder sb = new StringBuilder();

        if ((mask & NativeKeyEvent.CTRL_MASK) != 0 || (mask & 0x02) != 0) appendPart(sb, "Ctrl");
        if ((mask & NativeKeyEvent.SHIFT_MASK) != 0 || (mask & 0x01) != 0) appendPart(sb, "Shift");
        if ((mask & NativeKeyEvent.ALT_MASK) != 0 || (mask & 0x08) != 0) appendPart(sb, "Alt");
        if ((mask & NativeKeyEvent.META_MASK) != 0 || (mask & 0x04) != 0) appendPart(sb, "Meta");

        String key = NativeKeyEvent.getKeyText(hotkey.keyCode());
        appendPart(sb, key);

        return sb.toString();

    }

    private static void appendPart(StringBuilder sb, String part) {
        if (part == null || part.isBlank()) return;
        if (sb.length() > 0) sb.append(" + ");
        sb.append(part);
    }

}
