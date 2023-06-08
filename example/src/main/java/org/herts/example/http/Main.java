package org.herts.example.http;

import org.herts.http.HertsHttpEngine;
import org.herts.http.HertsHttpServer;
import org.herts.httpclient.HertsHttpClient;

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
        HertsHttpClient client = HertsHttpClient
                .builder("localhost")
                .registerHertService(HttpService.class)
                .secure(false)
                .build();

        var service = client.createHertsService(HttpService.class);
        var res = service.hellowWorld();
        System.out.println(res);
    }
}
