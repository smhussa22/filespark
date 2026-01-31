package com.filespark.client;

import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.filespark.javafx.BaseNotification;
import com.filespark.javafx.NotificationService;
import com.filespark.os.ClipboardUtil;
import com.filespark.Config;

public class UploadManager {

    public static final ExecutorService uploadPool = Executors.newFixedThreadPool(Config.maxUploads);

    public static void startUpload(File file){

        try { 

            String mime = Files.probeContentType(file.toPath());
            if (mime == null) mime = "application/octet-stream";

            NotificationService.show(new BaseNotification("Uploading: " + file.getName(), "default.png"));

            PresignResponse presignResponse = FastAPI.getPresignedUploadUrl(file, mime);
            UploadTask uploadTask = new UploadTask(file, presignResponse.uploadUrl, presignResponse.mime);

            uploadTask.setOnSucceeded(e -> {

                ClipboardUtil.copyToClipboard(presignResponse.viewUrl);
                NotificationService.show(new BaseNotification("Upload complete: " + file.getName() + "\nLink copied to clipboard: " + presignResponse.viewUrl, "success.png"));
                
            });

            uploadTask.setOnFailed(e -> {

                Throwable exception = uploadTask.getException();
                NotificationService.show(new BaseNotification("Upload failed: " + file.getName() + "\nError: " + exception.getMessage(), "error.png"));
            
            });

            uploadTask.setOnCancelled(e -> {

                NotificationService.show(new BaseNotification("Upload cancelled: " + file.getName(), "cancel.png"));
            
            });

            uploadPool.submit(uploadTask);

        }
        catch (Exception exception) { // @todo: specify exception

            System.err.println(exception.getMessage());
            NotificationService.show(new BaseNotification(file.getName() + " Upload error: " + exception.getMessage(), "error.png"));

        }

    }

}
