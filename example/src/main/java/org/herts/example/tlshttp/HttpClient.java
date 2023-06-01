package org.herts.example.tlshttp;

import org.herts.httpclient.HertsHttpClient;
import org.herts.httpclient.HertsHttpClientBase;

import java.util.Collections;

public class HttpClient {
    public static void run() {
        HertsHttpClientBase client = HertsHttpClient
                .builder("localhost")
                .registerHertService(HttpService.class)
                .secure(false)
                .port(Constant.port)
                .build();

        var service = client.createHertsService(HttpService01.class, Collections.singletonMap("Authorization", "Bearer XXXXXXX"));
    }
}
