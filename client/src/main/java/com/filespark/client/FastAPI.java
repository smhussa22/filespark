package com.filespark.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.*;
import org.json.JSONObject;

import com.filespark.Config;

public class FastAPI { 

    private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

    // @todo: specify exception
    public static PresignResponse getPresignedUploadUrl(File file, String mime) throws Exception {

        String fileName = file.getName();
        String url = Config.webDomain + "/presign-upload?filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + "&mime=" + URLEncoder.encode(mime, StandardCharsets.UTF_8);
        
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(url)).header("Authorization", "Bearer " + AppSession.getToken()).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) throw new RuntimeException("fastapi presign fail" + response.statusCode()); // @todo: maybe make this less harsh
        JSONObject json = new JSONObject(response.body());

        PresignResponse presignResponse = new PresignResponse();
        presignResponse.fileId = json.getString("fileId");
        presignResponse.key = json.getString("key");
        presignResponse.mime = json.getString("mime");
        presignResponse.extension = json.getString("extension");
        presignResponse.uploadUrl = json.getString("uploadUrl");
        presignResponse.viewUrl= json.getString("viewUrl");
        presignResponse.originalFilename = json.getString("originalFilename");
        
        return presignResponse;

    }


}