// holds any global variables in a protected manner

package com.filespark;

import java.util.List;

public class Config {

    private Config () {};

    // javafx
    public static final String WINDOW_TITLE = "FileSpark Uploader Test";
    public static final double WINDOW_WIDTH = 300;
    public static final double WINDOW_HEIGHT = 100;

    // FileScanner
    public static final int filesPerFetch = 1;
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
    public static final String webDomain = "http://127.0.0.1:8000";
}
