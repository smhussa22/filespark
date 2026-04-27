package com.filespark.client;

import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.filespark.client.FastAPI.EvictionPlan;
import com.filespark.client.FastAPI.QuotaExceededException;
import com.filespark.javafx.BaseNotification;
import com.filespark.javafx.EvictionConfirmDialog;
import com.filespark.javafx.NotificationService;
import com.filespark.os.ClipboardUtil;
import com.filespark.Config;

import javafx.application.Platform;

public class UploadManager {

    public static final ExecutorService uploadPool = Executors.newFixedThreadPool(Config.maxUploads);

    public static void startUpload(File file) {

        try {

            String mime = Files.probeContentType(file.toPath());
            if (mime == null) mime = "application/octet-stream";

            EvictionPlan plan;
            try {

                plan = FastAPI.previewEviction(file.length());

            }
            catch (Exception ex) {

                NotificationService.show(new BaseNotification("Could not check storage: " + ex.getMessage(), "error.png"));
                return;

            }

            if (plan.tooLargeForLimit) {

                NotificationService.show(new BaseNotification(
                    file.getName() + ": The total storage limit is " + formatBytes(plan.maxBytes) + ". This file is larger than that.",
                    "error.png"
                ));
                return;

            }

            if (!plan.fits) {

                NotificationService.show(new BaseNotification(
                    file.getName() + ": Upload would exceed the total quota of " + formatBytes(plan.maxBytes) + ".",
                    "error.png"
                ));
                return;

            }

            if (plan.evict != null && !plan.evict.isEmpty()) {

                CompletableFuture<Boolean> ask = new CompletableFuture<>();
                final long incomingBytes = file.length();
                final String fileName = file.getName();
                Platform.runLater(() -> {
                    boolean ok = EvictionConfirmDialog.show(null, fileName, incomingBytes, plan.maxBytes, plan.evict);
                    ask.complete(ok);
                });

                boolean confirmed;
                try { confirmed = ask.get(); }
                catch (Exception interruption) { confirmed = false; }

                if (!confirmed) {

                    NotificationService.show(new BaseNotification("Upload cancelled: " + fileName, "cancel.png"));
                    return;

                }

            }

            NotificationService.show(new BaseNotification("Uploading: " + file.getName(), "default.png"));

            PresignResponse presignResponse;
            try {

                presignResponse = FastAPI.getPresignedUploadUrl(file, mime);

            }
            catch (QuotaExceededException quotaException) {

                NotificationService.show(new BaseNotification(file.getName() + ": " + quotaException.getMessage(), "error.png"));
                return;

            }

            UploadTask uploadTask = new UploadTask(file, presignResponse.uploadUrl, presignResponse.mime);

            uploadTask.setOnSucceeded(e -> {

                String link = buildShareLink(presignResponse.fileId);
                ClipboardUtil.copyToClipboard(link);
                NotificationService.show(new BaseNotification("Upload complete: " + file.getName() + "\nLink copied to clipboard", "success.png"));

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
        catch (Exception exception) {

            NotificationService.show(new BaseNotification(file.getName() + " Upload error: " + exception.getMessage(), "error.png"));

        }

    }

    private static String buildShareLink(String fileId) {

        String userId = AppSession.getUser().map(User::getId).orElse(null);
        if (userId == null || fileId == null) return Config.frontendDomain;
        return Config.frontendDomain + "/f/" + userId + "/" + fileId;

    }

    private static String formatBytes(long bytes) {

        if (bytes < 1024) return bytes + " B";
        String[] units = { "KB", "MB", "GB", "TB" };
        double value = bytes / 1024.0;
        int unit = 0;
        while (value >= 1024 && unit < units.length - 1) {

            value /= 1024;
            unit++;

        }
        if (value >= 10) return String.format("%.0f %s", value, units[unit]);
        return String.format("%.1f %s", value, units[unit]);

    }

}
