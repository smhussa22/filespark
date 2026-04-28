package com.filespark.client;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import com.filespark.App;

import javafx.application.HostServices;

public final class BrowserLauncher {

    private BrowserLauncher() {}

    /**
     * Opens the given URL in the user's default browser. Tries (in order)
     * JavaFX HostServices, java.awt.Desktop, and a direct Windows shell-out.
     * Returns true if any path reported success.
     */
    public static boolean open(URI uri) {

        if (uri == null) return false;
        String url = uri.toString();

        // 1. JavaFX HostServices — works on packaged jlink runtimes where AWT may not.
        try {

            HostServices hs = App.hostServicesRef();
            if (hs != null) {
                hs.showDocument(url);
                return true;
            }

        }
        catch (Throwable ignored) {}

        // 2. java.awt.Desktop — works in dev / on full JDK.
        try {

            if (Desktop.isDesktopSupported()) {
                Desktop d = Desktop.getDesktop();
                if (d.isSupported(Desktop.Action.BROWSE)) {
                    d.browse(uri);
                    return true;
                }
            }

        }
        catch (Throwable ignored) {}

        // 3. Windows shell-out — last resort. Empty "" is the window-title arg start expects.
        try {

            String os = System.getProperty("os.name", "").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "start", "", url).start();
                return true;
            }
            if (os.contains("mac")) {
                new ProcessBuilder("open", url).start();
                return true;
            }
            new ProcessBuilder("xdg-open", url).start();
            return true;

        }
        catch (IOException ignored) {}

        return false;

    }

}
