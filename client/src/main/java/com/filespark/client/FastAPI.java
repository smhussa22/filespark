package com.filespark.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filespark.Config;

public class FastAPI {

    private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    private static final ObjectMapper mapper = new ObjectMapper();

    // @todo: specify exception
    public static PresignResponse getPresignedUploadUrl(File file, String mime) throws Exception {

        String fileName = file.getName();
        long sizeBytes = file.length();

        String url = Config.webDomain + "/presign-upload"
                + "?filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                + "&mime=" + URLEncoder.encode(mime, StandardCharsets.UTF_8)
                + "&sizeBytes=" + sizeBytes;

        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(url)).header("Authorization", "Bearer " + AppSession.getToken()).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 413) {

            String message = extractErrorMessage(response.body(), "File exceeds the 250 MB limit.");
            throw new QuotaExceededException(message);

        }

        if (response.statusCode() != 200) {

            String message = extractErrorMessage(response.body(), "presign failed: HTTP " + response.statusCode());
            throw new RuntimeException(message);

        }

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

    private static String extractErrorMessage(String body, String fallback) {

        if (body == null || body.isEmpty()) return fallback;
        try {

            JSONObject json = new JSONObject(body);
            if (json.has("error")) return json.getString("error");

        }
        catch (Exception ignored) {}
        return fallback;

    }

    public static class QuotaExceededException extends RuntimeException {

        public QuotaExceededException(String message) {

            super(message);

        }

    }

    public static List<LinkSummary> listMyFiles() throws Exception {

        String url = Config.webDomain + "/files";
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + AppSession.getToken())
                .GET().build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) throw new RuntimeException("listMyFiles failed: HTTP " + response.statusCode() + " body=" + response.body());

        LinkSummary[] arr = mapper.readValue(response.body(), LinkSummary[].class);
        return Arrays.asList(arr);

    }

    public static void deleteAccount() throws Exception {

        String url = Config.webDomain + "/users/me";
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + AppSession.getToken())
                .DELETE().build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();
        if (code != 200 && code != 204) throw new RuntimeException("deleteAccount failed: HTTP " + code + " body=" + response.body());

    }

    public static long[] getStorageUsage() throws Exception {

        String url = Config.webDomain + "/storage/usage";
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + AppSession.getToken())
                .GET().build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) throw new RuntimeException("getStorageUsage failed: HTTP " + response.statusCode() + " body=" + response.body());

        JSONObject json = new JSONObject(response.body());
        return new long[] { json.getLong("usedBytes"), json.getLong("maxBytes") };

    }

    public static void setVisibility(String fileId, String visibility) throws Exception {

        String url = Config.webDomain + "/files/" + URLEncoder.encode(fileId, StandardCharsets.UTF_8) + "/visibility";
        String body = "{\"visibility\":\"" + visibility + "\"}";

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + AppSession.getToken())
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();
        if (code != 200 && code != 204) throw new RuntimeException("setVisibility failed: HTTP " + code + " body=" + response.body());

    }

    public static void deleteFile(String fileId) throws Exception {

        String url = Config.webDomain + "/files/" + URLEncoder.encode(fileId, StandardCharsets.UTF_8);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + AppSession.getToken())
                .DELETE().build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();
        if (code != 200 && code != 204) throw new RuntimeException("deleteFile failed: HTTP " + code + " body=" + response.body());

    }

}