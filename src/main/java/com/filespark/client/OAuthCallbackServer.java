package com.filespark.client;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpServer;

import javafx.scene.Scene;

public class OAuthCallbackServer {
    
    private static HttpServer server;

    public static void start(Scene scene) throws Exception {

        server = HttpServer.create(new InetSocketAddress(8765), 0);
        server.createContext("/callback", exchange -> {

            URI uri = exchange.getRequestURI();
            String query = uri.getQuery();
            String code = OAuthCallbackHandler.extract(query, "code");

            if (code != null) OAuthCallbackHandler.exchangeCode(code, scene);
            String response = "Login successful. You may return to FileSpark.";
            exchange.sendResponseHeaders(200, response.length());

            try (OutputStream os = exchange.getResponseBody()) {

                os.write(response.getBytes(StandardCharsets.UTF_8));

            }

            server.stop(0);

        });

        server.start();

    }

}
