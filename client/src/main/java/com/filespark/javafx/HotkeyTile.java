package com.filespark.javafx;

import java.util.function.Consumer;
import com.filespark.OperatingSystem;
import com.filespark.os.Hotkey;
import com.filespark.os.HotkeyManager;
import com.filespark.os.HotkeyUtil;
import com.filespark.os.OperatingSystems;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import com.filespark.Config;

public class HotkeyTile extends HBox {

    private Hotkey hotkey;
    private final Label hotkeyString;

    private static final String STYLE_TILE_BASE =
        "-fx-background-color: " + Config.bgSurface + ";" +
        "-fx-background-radius: 10;" +
        "-fx-border-color: " + Config.borderSubtle + ";" +
        "-fx-border-width: 1;" +
        "-fx-border-radius: 10;";
    private static final String STYLE_TILE_HOVER =
        "-fx-background-color: " + Config.bgElevated + ";" +
        "-fx-background-radius: 10;" +
        "-fx-border-color: " + Config.borderStrong + ";" +
        "-fx-border-width: 1;" +
        "-fx-border-radius: 10;";

    private static final String STYLE_KBD_BASE =
        "-fx-background-color: " + Config.bgElevated + ";" +
        "-fx-background-radius: 6;" +
        "-fx-border-color: " + Config.borderSubtle + ";" +
        "-fx-border-width: 1;" +
        "-fx-border-radius: 6;" +
        "-fx-text-fill: " + Config.textPrimary + ";" +
        "-fx-font-size: 12px;" +
        "-fx-font-weight: 600;" +
        "-fx-padding: 6 10;";
    private static final String STYLE_KBD_HOVER =
        "-fx-background-color: " + Config.bgHover + ";" +
        "-fx-background-radius: 6;" +
        "-fx-border-color: " + Config.accent + ";" +
        "-fx-border-width: 1;" +
        "-fx-border-radius: 6;" +
        "-fx-text-fill: " + Config.textPrimary + ";" +
        "-fx-font-size: 12px;" +
        "-fx-font-weight: 600;" +
        "-fx-padding: 6 10;";

    public HotkeyTile(String actionText, Hotkey initialHotkey, Consumer<Hotkey> onHotkeyChanged) {

        this.hotkey = initialHotkey;

        Label actionLabel = new Label(actionText);
        actionLabel.setTextFill(Color.web(Config.textPrimary));
        actionLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 500;");

        hotkeyString = new Label(formatHotkey(initialHotkey));
        hotkeyString.setStyle(STYLE_KBD_BASE);
        hotkeyString.setCursor(javafx.scene.Cursor.HAND);
        hotkeyString.setOnMouseEntered(e -> hotkeyString.setStyle(STYLE_KBD_HOVER));
        hotkeyString.setOnMouseExited(e -> hotkeyString.setStyle(STYLE_KBD_BASE));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        setAlignment(Pos.CENTER_LEFT);
        setSpacing(Config.space3);
        setPadding(new Insets(Config.space3, Config.space4, Config.space3, Config.space4));
        setStyle(STYLE_TILE_BASE);
        setOnMouseEntered(e -> setStyle(STYLE_TILE_HOVER));
        setOnMouseExited(e -> setStyle(STYLE_TILE_BASE));

        getChildren().addAll(actionLabel, spacer, hotkeyString);

        hotkeyString.setOnMouseClicked(e -> beginRecording(onHotkeyChanged));

    }

    private void beginRecording(Consumer<Hotkey> onHotkeyChanged) {

        if (!HotkeyManager.isNativeHookActive()) {

            // Without the global key hook, no native key events ever reach
            // a recorder — clicks would just sit on "Press keys…" forever.
            // Tell the user what's wrong and bail out.
            String msg = OperatingSystem.OS == OperatingSystems.MAC
                ? "Grant Accessibility permission first"
                : "Hotkeys unavailable";
            hotkeyString.setText(msg);
            return;

        }

        hotkeyString.setText("Press up to " + HotkeyUtil.MAX_KEYS + " keys…");

        NativeKeyListener recorder = new NativeKeyListener() {

            @Override
            public void nativeKeyPressed(NativeKeyEvent e) {

                int keyCode = e.getKeyCode();

                if (isModifierKey(keyCode)) return;

                int normalizedMask = HotkeyUtil.normalizeMask(e.getModifiers());

                if (HotkeyUtil.countModifiers(normalizedMask) > HotkeyUtil.MAX_MODIFIERS) {

                    // Reject combos longer than MAX_KEYS (modifiers + main key).
                    // Keep the recorder attached so the user can release some
                    // modifiers and try again without re-clicking the tile.
                    Platform.runLater(() ->
                        hotkeyString.setText("Max " + HotkeyUtil.MAX_KEYS + " keys — try again")
                    );
                    return;

                }

                Hotkey newHotkey = new Hotkey(normalizedMask, keyCode);

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
