package org.hertsstack.example.jwthttp;

import org.hertsstack.core.exception.http.HttpErrorException;
import org.hertsstack.http.HertsHttpEngine;
import org.hertsstack.http.HertsHttpServer;
import org.hertsstack.httpclient.HertsHttpClient;

import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        startServer();
        startClient();
        System.exit(0);
    }

    private static void startServer() {
        HertsHttpEngine engine = HertsHttpServer.builder()
                .registerHertsHttpService(new HttpServiceImpl(), new JwtServerInterceptor())
                .registerHertsHttpService(new AuthHttpServiceImpl())
                .build();

        Thread t = new Thread(engine::start);
        t.start();
    }

    private static void startClient() {
        HertsHttpClient client = HertsHttpClient
                .builder("localhost")
                .registerHertService(HttpService.class)
                .registerHertService(AuthHttpService.class)
                .secure(false)
                .build();

        var service = client.createHertsService(HttpService.class);
        try {
            service.hellowWorld();
        } catch (HttpErrorException ex) {
            System.out.println("Error on client: " + ex.getMessage());
        }

        // Authentication
        var authService = client.createHertsService(AuthHttpService.class);
        var token = authService.signIn("email", "password");

        // Recreate service with token
        service = client.recreateHertsService(HttpService.class, Collections.singletonMap("Authorization", token));
        var res = service.hellowWorld();
        System.out.println("Received data on client: " + res);
    }
}
