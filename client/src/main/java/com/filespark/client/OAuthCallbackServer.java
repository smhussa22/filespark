package com.filespark.client;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import com.filespark.Config;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;


public final class OAuthCallbackServer {

    private static HttpServer server;
    private static int port;

    public static synchronized int start() throws Exception {

        if (server != null) return port;

        server = HttpServer.create(new InetSocketAddress(0), 0);
        port = server.getAddress().getPort();

        server.createContext("/callback", exchange -> {

            boolean shouldStop = false;
            try {

                applyCors(exchange);

                if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {

                    exchange.sendResponseHeaders(204, -1);
                    return;

                }

                if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {

                    sendText(exchange, 405, "Method Not Allowed");
                    return;

                }

                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                OAuthCallbackHandler.handleHandoff(body);
                sendText(exchange, 200, "Login successful. You may return to FileSpark.");
                shouldStop = true;

            }
            catch (Exception ex) {

                ex.printStackTrace();
                try { sendText(exchange, 500, "Login failed: " + ex.getMessage()); } catch (Exception ignored) {}
                shouldStop = true;

            }
            finally {

                if (shouldStop) {
                    new Thread(OAuthCallbackServer::stop).start();
                }

            }

        });

        server.start();
        return port;

    }

    private static final Set<String> ALLOWED_ORIGINS = buildAllowedOrigins();

    private static Set<String> buildAllowedOrigins() {

        String base = Config.frontendDomain;
        try {

            URI u = URI.create(base);
            String host = u.getHost();
            String scheme = u.getScheme();
            String withWww = host.startsWith("www.") ? host : "www." + host;
            String withoutWww = host.startsWith("www.") ? host.substring(4) : host;
            return Set.of(
                scheme + "://" + withoutWww,
                scheme + "://" + withWww
            );

        } catch (Exception e) {

            return Set.of(base);

        }

    }

    private static void applyCors(HttpExchange exchange) {

        String origin = exchange.getRequestHeaders().getFirst("Origin");
        String allowed = (origin != null && ALLOWED_ORIGINS.contains(origin)) ? origin : Config.frontendDomain;
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", allowed);
        exchange.getResponseHeaders().set("Vary", "Origin");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().set("Access-Control-Allow-Private-Network", "true");
        exchange.getResponseHeaders().set("Access-Control-Max-Age", "600");

    }

    private static void sendText(HttpExchange exchange, int status, String text) throws java.io.IOException {

        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {

            os.write(bytes);

        }

    }

    public static synchronized void stop() {

        if (server != null) {

            server.stop(0);
            server = null;

        }

    }

    public static int getPort() { return port; }

}
