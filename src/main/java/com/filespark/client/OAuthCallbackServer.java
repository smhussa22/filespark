package com.filespark.client;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpServer;


public final class OAuthCallbackServer {
    
    private static HttpServer server;
    private static int port;

    public static synchronized int start() throws Exception {

        if (server != null) return port;

        server = HttpServer.create(new InetSocketAddress(0), 0);
        port = server.getAddress().getPort();

        server.createContext("/callback", exchange -> {

            try {

                URI uri = exchange.getRequestURI();
                String code = OAuthCallbackHandler.extract(uri.getQuery(), "code");

                if (code != null) OAuthCallbackHandler.exchangeCode(code);
                
                String response = "Login successful. You may return to FileSpark.";
                exchange.sendResponseHeaders(200, response.getBytes().length);

                try (OutputStream os = exchange.getResponseBody()) {

                    os.write(response.getBytes(StandardCharsets.UTF_8));

                }

            }
            finally {

                stop();

            }
            

        });

        server.start();
        return port;

    }

    public static synchronized void stop() {

        if (server != null) {

            server.stop(0);
            server = null;

        }

    }

    public static int getPort() { return port; }

}
