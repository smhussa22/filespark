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

}
