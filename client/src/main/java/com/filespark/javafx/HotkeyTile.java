package com.filespark.javafx;

import java.util.function.Consumer;
import com.filespark.os.Hotkey;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import com.filespark.Config;
public class HotkeyTile extends HBox {

    private Hotkey hotkey;
    private final Label hotkeyLabel;
    private final Label hotkeyString;

    public HotkeyTile(String actionText, Hotkey initialHotkey, Consumer<Hotkey> onHotkeyChanged) {

        this.hotkey = initialHotkey;

        hotkeyLabel = new Label(actionText);
        hotkeyLabel.setStyle("-fx-font-size: 18px;" + "-fx-text-fill: " + Config.mainOrange + ";" + "-fx-padding: 10 16;" + "-fx-background-color: " + Config.mainBlack + ";" +
            "-fx-background-radius: 10;" + "-fx-border-radius: 10;" + "-fx-border-color: " + Config.mainOrange + ";" + "-fx-border-width: 1.5;"
        );

        hotkeyString = new Label(formatHotkey(initialHotkey));
        hotkeyString.setStyle("-fx-font-size: 18px;" + "-fx-text-fill: " + Config.mainOrange + ";" + "-fx-padding: 10 16;" + "-fx-background-color: " + Config.mainBlack + ";"
            + "-fx-background-radius: 10;" + "-fx-border-radius: 10;" + "-fx-border-color: " + Config.mainOrange + ";" + "-fx-border-width: 1.5;"
        );

        hotkeyString.setCursor(javafx.scene.Cursor.HAND);

        hotkeyString.setOnMouseEntered(e ->

            hotkeyString.setStyle(hotkeyString.getStyle() + "-fx-background-color: " + Config.mainGrey + ";")

        );

        hotkeyString.setOnMouseExited(e ->

            hotkeyString.setStyle(hotkeyString.getStyle().replace("-fx-background-color: " + Config.mainGrey + ";" , "-fx-background-color: " + Config.mainBlack + ";"))

        );

        setAlignment(Pos.CENTER_LEFT);
        setSpacing(12);

        HBox.setHgrow(hotkeyLabel, Priority.ALWAYS);

        getChildren().addAll(hotkeyLabel, hotkeyString);

        hotkeyString.setOnMouseClicked(e -> beginRecording(onHotkeyChanged));

    }

    private void beginRecording(Consumer<Hotkey> onHotkeyChanged) {

        hotkeyString.setText("Press keys…");

        NativeKeyListener recorder = new NativeKeyListener() {
            
            @Override
            public void nativeKeyPressed(NativeKeyEvent e) {

                int keyCode = e.getKeyCode();

                if (isModifierKey(keyCode)) return; 

                int cleanMask = e.getModifiers() & 0x0F;
                Hotkey newHotkey = new Hotkey(cleanMask, keyCode);

                Platform.runLater(() -> {

                    hotkey = newHotkey;
                    hotkeyString.setText(formatHotkey(newHotkey));
                    onHotkeyChanged.accept(newHotkey);

                });

                GlobalScreen.removeNativeKeyListener(this);

            }

        };

        GlobalScreen.addNativeKeyListener(recorder);

    }

    private static boolean isModifierKey(int keyCode) {

        return keyCode == NativeKeyEvent.VC_SHIFT 
        || keyCode == NativeKeyEvent.VC_CONTROL 
        || keyCode == NativeKeyEvent.VC_ALT 
        || keyCode == NativeKeyEvent.VC_META
        || keyCode == NativeKeyEvent.VC_TAB;
        
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
