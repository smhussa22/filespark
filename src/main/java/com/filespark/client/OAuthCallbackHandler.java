package com.filespark.client;

import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filespark.scenes.Client;

import javafx.application.Platform;
import javafx.scene.Scene;

public class OAuthCallbackHandler {

    private OAuthCallbackHandler(){}

    private static final ObjectMapper mapper = new ObjectMapper();
    
    public static String extract(String query, String key) { 
    
        if (query == null) return null;
        
        for (String part : query.split("&")) {

            String[] keyValue = part.split("=");
            if (keyValue.length == 2 && keyValue[0].equals(key)) return URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);

        }
        
        return null; 
    
    }

    public static void exchangeCode(String code, Scene scene) {

        try {

            String body = mapper.writeValueAsString(Map.of("code", code));

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8000/auth/google/session")).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(body)).build();

            HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(response -> {

                if (response.statusCode() != 200) {

                    System.err.println("OAuth exchange failed: " + response.body());
                    return;

                }

                try {

                    //r

                } 
                catch (Exception e) {

                    e.printStackTrace();

                }

            });

        } 
        catch (Exception e) {

            e.printStackTrace();

        }

    }

}
