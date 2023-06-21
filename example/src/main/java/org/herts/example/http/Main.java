package org.herts.example.http;

import org.herts.core.context.HertsMetricsSetting;
import org.herts.http.HertsHttpEngine;
import org.herts.http.HertsHttpServer;
import org.herts.httpclient.HertsHttpClient;

public class Main {
    public static void main(String[] args) {
        startServer();
        startClient();
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {


        }
        System.exit(0);
    }

    private static void startServer() {
        HertsMetricsSetting metrics = HertsMetricsSetting.builder()
                .isRpsEnabled(true)
                .isLatencyEnabled(true)
                .isErrRateEnabled(true)
                .isServerResourceEnabled(true)
                .isJvmEnabled(true)
                .build();

        HertsHttpEngine engine = HertsHttpServer.builder()
                .registerHertsHttpService(new HttpServiceImpl())
                .setMetricsSetting(metrics)
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
        var res = service.helloWorld();
        System.out.println(res);
    }
}
