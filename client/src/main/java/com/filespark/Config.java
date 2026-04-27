// holds any global variables in a protected manner

package com.filespark;

import java.util.List;

public class Config {

    private Config () {};

    // javafx
    public static final String WINDOW_TITLE = "FileSpark";
    public static final double WINDOW_WIDTH = 750;
    public static final double WINDOW_HEIGHT = 750;
    public static final double BOTTOM_RIGHT_CONTAINER_HEIGHT = 360;
    public static final double BOTTOM_RIGHT_CONTAINER_WIDTH = 420;

    // FileScanner
    public static final int filesPerFetch = 25;
    public static final List<String> allowedExtensions = List.of(".png", ".jpg", ".jpeg", ".webp", ".mp4", ".mov", ".mkv", ".pdf", ".docx", ".txt", ".zip", ".rar", ".7z", ".heic");

    // Windows Stuff
    public static final String WIN32_CLASS_ID_OBJECT_SHELL_LINK = "{00021401-0000-0000-C000-000000000046}";
    public static final String WIN32_INTERFACE_ID_SHELL_LINK_WIDE = "{000214F9-0000-0000-C000-000000000046}";
    public static final String WIN32_INTERFACE_ID_PERSIST_FILE = "{0000010B-0000-0000-C000-000000000046}";
    public static final int WIN32_MAX_PATH = 260;
    public static final int WIN32_VTABLE_GETPATH = 3;
    public static final int WIN32_VTABLE_RESOLVE = 19;
    public static final int WIN32_VTABLE_LOAD = 5;

    // api stuff — public defaults baked in; override with -Dfilespark.webDomain=... or FILESPARK_WEB_DOMAIN env var for dev
    public static final String DEFAULT_WEB_DOMAIN = "https://api.getfilespark.tech";
    public static final String DEFAULT_FRONTEND_DOMAIN = "https://getfilespark.tech";
    public static final String webDomain = resolve("filespark.webDomain", "FILESPARK_WEB_DOMAIN", DEFAULT_WEB_DOMAIN);
    public static final String frontendDomain = resolve("filespark.frontendDomain", "FILESPARK_FRONTEND_DOMAIN", DEFAULT_FRONTEND_DOMAIN);

    private static String resolve(String sysProp, String envVar, String fallback) {

        String v = System.getProperty(sysProp);
        if (v != null && !v.isBlank()) return stripTrailingSlash(v);
        v = System.getenv(envVar);
        if (v != null && !v.isBlank()) return stripTrailingSlash(v);
        return fallback;

    }

    private static String stripTrailingSlash(String s) {

        return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;

    }
    
    // legacy color aliases — keep for code that still references them
    public static final String mainBlack = "#1a1818";
    public static final String mainOrange = "#d95923";
    public static final String altOrange = "#a14018";
    public static final String mainGrey = "#2a2727";

    // design tokens — single source of truth for the desktop UI
    // surfaces (darkest -> lightest)
    public static final String bgBase     = "#161414";   // window background
    public static final String bgSurface  = "#1f1d1d";   // cards, sidebar, top bar
    public static final String bgElevated = "#2a2727";   // hover state, popovers, inputs
    public static final String bgHover    = "#272424";   // subtle hover

    // borders
    public static final String borderSubtle = "#332f2f"; // hairline dividers
    public static final String borderStrong = "#403a3a"; // emphasised separators

    // text
    public static final String textPrimary   = "#ECECEC";
    public static final String textSecondary = "#9b9494";
    public static final String textMuted     = "#6c6464";

    // accent
    public static final String accent      = "#d95923";          // brand orange
    public static final String accentDim   = "#a14018";          // pressed
    public static final String accentSoft  = "rgba(217,89,35,0.14)"; // tinted backgrounds

    // status
    public static final String success = "#4ade80";
    public static final String danger  = "#ef4444";
    public static final String warning = "#eab308";

    // radii (px)
    public static final int radiusSm = 6;
    public static final int radiusMd = 8;
    public static final int radiusLg = 12;

    // spacing (px) — use multiples of 4 for everything
    public static final int space1 = 4;
    public static final int space2 = 8;
    public static final int space3 = 12;
    public static final int space4 = 16;
    public static final int space5 = 20;
    public static final int space6 = 24;

    // tile / layout sizing
    public static final int fileTileNameLength = 25;
    public static final double fileTileWidth = 300;
    public static final double fileTileHeight = 200;
    public static final String defaultEmail = "filesparkuser@filespark.com";
    public static final String defaultName = "FileSpark User";
    public static final int maxNotifications = 3;

    // multithread/concurrency stuff
    public static final int maxUploads = 3;

}
