package com.filespark;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class uploadFileToS3 {

    public static void uploadFileViaFastAPI(File file){

        try {

            String boundary = "----JavaFormBoundary" + System.currentTimeMillis();
            URL requestUrl = new URL(Config.webDomain + "/upload");

            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            OutputStream output = connection.getOutputStream();
            output.write(("--" + boundary + "\r\n").getBytes());
            output.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\n").getBytes());
            output.write(("Content-Type: application/octet-stream\r\n\r\n").getBytes());
            Files.copy(file.toPath(), output);
            output.write("\r\n".getBytes());
            output.write(("--" + boundary + "--\r\n").getBytes());
            output.flush();
            output.close();

            int responseCode = connection.getResponseCode();
            System.out.println("Upload response code: " + responseCode);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) { System.out.println("Server response: " + line); }

            reader.close();
            connection.disconnect();

        }
        catch(Exception exception) {

            System.err.println("[uploadFileToS3.java]: " + exception);

        }

    }
    
}
