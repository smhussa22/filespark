// holds any global variables in a protected manner

package com.filespark;

import java.util.List;

public class Config {

    private Config () {};

    // javafx
    public static final String WINDOW_TITLE = "FileSpark";
    public static final double WINDOW_WIDTH = 750;
    public static final double WINDOW_HEIGHT = 750;

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

    // api stuff
    public static final String webDomain = "http://localhost:8000";

    // javafx stuff
    public static final String mainBlack = "#212020";
    public static final String mainOrange = "#BA4700";
    public static final String mainGrey = "#2f2c2c";
    public static final int fileTileNameLength = 25;
    public static final String defaultEmail = "filesparkuser@filespark.com";
    public static final String defaultName = "FileSpark User";

    // multithread/concurrency stuff
    public static final int maxUploads = 3;

}
