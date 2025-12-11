package com.filespark.client;

import java.net.HttpURLConnection; // @todo switch to httpclient
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.*;
import org.json.JSONObject;

import com.filespark.Config;

public class FastAPI { //@todo wrap this in tries

    public static PresignResponse getPresignedUploadUrl(File file, String mime) throws Exception {

        String fileName = file.getName();
        System.out.println(fileName);

        String urlString = Config.webDomain + "/presign-upload?filename=" 
            + URLEncoder.encode(fileName, StandardCharsets.UTF_8)
            + "&mime="
            + URLEncoder.encode(mime, StandardCharsets.UTF_8);

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder responseString = new StringBuilder();
        String line;

        while((line = reader.readLine()) != null){

            responseString.append(line);

        }

        reader.close();

        JSONObject json = new JSONObject(responseString.toString());

        PresignResponse response = new PresignResponse();
        response.fileId = json.getString("fileId");
        response.key = json.getString("key");
        response.mime = json.getString("mime");
        response.extension = json.getString("extension");
        response.uploadUrl = json.getString("uploadUrl");
        response.viewUrl= json.getString("viewUrl");
        response.originalFilename = json.getString("originalFilename");
        
        if (connection != null) connection.disconnect();
        
        return response;

    }


}