package com.filespark.client;

import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.filespark.Config;

public class UploadManager {

    public static final ExecutorService uploadPool = Executors.newFixedThreadPool(Config.maxUploads);

    public static void startUpload(File file){

        try { 

            String mime = Files.probeContentType(file.toPath());
            if (mime == null) mime = "application/octet-stream";

            PresignResponse presignResponse = FastAPI.getPresignedUploadUrl(file, mime);
            UploadTask uploadTask = new UploadTask(file, presignResponse.uploadUrl, presignResponse.mime);

            uploadTask.setOnSucceeded(e -> {

                System.out.println("Upload complete!");
                System.out.println("File ID: " + presignResponse.fileId);
                System.out.println("View URL: " + presignResponse.viewUrl);

            });

            uploadTask.setOnFailed(e -> {

                System.err.println("Upload failed: " + uploadTask.getException());

            });

            uploadTask.setOnCancelled(e -> {

                System.out.println("Upload cancelled.");

            });

            uploadPool.submit(uploadTask);

        }
        catch (Exception exception) {

            System.err.println(exception.getMessage());

        }

    }

}
