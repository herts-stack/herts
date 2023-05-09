package org.herts.example.server;

import org.herts.example.HttpServiceImpl01;
import org.herts.common.context.HertsMetricsSetting;
import org.herts.example.HttpServiceImpl02;
import org.herts.http.engine.HertsHttpEngine;
import org.herts.http.engine.HertsHttpServer;

public class HttpExample {

    public static void run() {
        var metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();

        HertsHttpEngine engine = HertsHttpServer.builder()
                .registerHertsHttpService(new HttpServiceImpl01(), new HttpServerInterceptor())
                .registerHertsHttpService(new HttpServiceImpl02())
                .setMetricsSetting(metrics)
                .build();

        engine.start();
    }
}
