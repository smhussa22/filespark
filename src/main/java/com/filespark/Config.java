// holds any global variables in a protected manner

package com.filespark;

import software.amazon.awssdk.regions.Region;

import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {

    private Config () {};
    private static Dotenv dotenv = Dotenv.load();

    // javafx
    public static final String WINDOW_TITLE = "FileSpark Uploader Test";
    public static final double WINDOW_WIDTH = 300;
    public static final double WINDOW_HEIGHT = 100;

    // S3
    private static final String S3_ACCESS_KEY = dotenv.get("AWS_ACCESS_KEY_ID");
    private static final String S3_SECRET_KEY = dotenv.get("AWS_SECRET_ACCESS_KEY");
    public static final String S3_BUCKET_NAME = dotenv.get("AWS_BUCKET_NAME");
    public static final Region S3_REGION = Region.US_EAST_2;
    public static final String S3_UPLOAD_STRING_KEY = "uploads/";

    public static String getS3AccessKey() { return S3_ACCESS_KEY; }
    public static String getS3SecretKey() { return S3_SECRET_KEY; }

    // FileScanner
    public static final int filesPerFetch = 25;
    public static final List<String> allowedExtensions = List.of(".png", ".jpg", ".jpeg", ".webp", ".mp4", ".mov", ".mkv", ".pdf", ".docx", ".txt", ".zip", ".rar", ".7z", ".heic");

    // Windows Stuff
    public static final String WIN32_CLASS_ID_OBJECT_SHELL_LINK = "{00021401-0000-0000-C000-000000000046}";
    public static final String WIN32_INTERFACE_ID_SHELL_LINK_WIDE = "{000214F9-0000-0000-C000-000000000046}";
    public static final String WIN32_INTERFACE_ID_PERSIST_FILE = "{0000010B-0000-0000-C000-000000000046}";
    public static final int WIN32_MAX_PATH = 260;
    public static final int WIN32_VTABLE_GETPATH = 20;
    public static final int WIN32_VTABLE_RESOLVE = 21;
    public static final int WIN32_VTABLE_LOAD = 5;

}
