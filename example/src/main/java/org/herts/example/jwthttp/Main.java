package org.herts.example.jwthttp;

import org.herts.http.engine.HertsHttpEngine;
import org.herts.http.engine.HertsHttpServer;
import org.herts.httpclient.HertsHttpClient;
import org.herts.httpclient.HertsHttpClientBase;

public class Main {
    public static void main(String[] args) {
        startServer();
        startClient();
        System.exit(0);
    }

    private static void startServer() {
        HertsHttpEngine engine = HertsHttpServer.builder()
                .registerHertsHttpService(new HttpServiceImpl())
                .build();

        Thread t = new Thread(engine::start);
        t.start();
    }

    private static void startClient() {
        HertsHttpClientBase client = HertsHttpClient
                .builder("localhost")
                .registerHertService(HttpService.class)
                .secure(false)
                .build();

        var service = client.createHertsService(HttpService.class);
        var res = service.hellowWorld();
        System.out.println(res);
    }
}
