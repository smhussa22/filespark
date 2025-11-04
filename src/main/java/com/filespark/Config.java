package com.filespark;

import software.amazon.awssdk.regions.Region;

public class Config {

    private Config () {};

    // javafx
    public static final String WINDOW_TITLE = "FileSpark Uploader Test";
    public static final double WINDOW_WIDTH = 300;
    public static final double WINDOW_HEIGHT = 100;

    // S3
    public static final String S3_BUCKET_NAME = System.getenv("AWS_BUCKET_NAME");
    public static final Region S3_REGION = Region.US_EAST_2;
    public static final String S3_UPLOAD_STRING_KEY = "uploads/";

}
