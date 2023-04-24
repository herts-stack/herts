package org.herts.example.server;

import org.herts.example.HttpServerInterceptor;
import org.herts.example.HttpServiceImpl;
import org.herts.common.context.HertsMetricsSetting;
import org.herts.http.engine.HertsHttpEngine;
import org.herts.http.engine.HertsHttpServer;

public class HttpExample {

    public static void run() {
        var metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();

        HertsHttpEngine engine = HertsHttpServer.builder()
                .addImplementationService(new HttpServiceImpl())
                .setInterceptor(new HttpServerInterceptor())
                .setMetricsSetting(metrics)
                .build();

        engine.start();
    }
}
