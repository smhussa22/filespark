package com.filespark.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import javafx.concurrent.Task;

public class UploadTask extends Task<Void>{
    
    private final File file;
    private final String presignedUrl;
    private final String mime;
    private final int kiloByte = 1024;

    public UploadTask(File file, String presignedUrl, String mime) {

        this.file = file;
        this.presignedUrl = presignedUrl;
        this.mime = mime;

    }

    @Override
    protected Void call() throws Exception{

        long fileBytes = file.length();
        long uploaded = 0;

        URL url = new URL(presignedUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", mime);

         try (OutputStream out = connection.getOutputStream(); FileInputStream in = new FileInputStream(file)) {

            byte[] buffer = new byte[32 * kiloByte];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {

                out.write(buffer, 0, bytesRead);
                uploaded += bytesRead;
                updateProgress(uploaded, fileBytes);
                updateMessage(String.format("Uploading %d / %d bytes", uploaded, fileBytes));

                if (isCancelled()) {

                    connection.disconnect();
                    break;

                }

            }

        }

        return null;

    }

}
