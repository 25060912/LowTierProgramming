package app;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import logic.ghazy;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class backend {
    public static void main(String[] args) throws IOException {

        ghazy analyzer = new ghazy();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/analyze", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

                if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
                }

                if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                    exchange.sendResponseHeaders(405, -1);
                    return;
                }

                String journal = new String(
                        exchange.getRequestBody().readAllBytes(),
                        StandardCharsets.UTF_8
                );

                String result = analyzer.analyze(journal);

                exchange.sendResponseHeaders(200, result.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(result.getBytes());
                os.close();
            }
        });

        server.start();
        System.out.println("Server running at http://localhost:8080");
    }
}