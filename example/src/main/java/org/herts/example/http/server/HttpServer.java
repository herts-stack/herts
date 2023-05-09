package org.herts.example.http.server;

import org.herts.example.common.Constant;
import org.herts.example.http.HttpServiceImpl01;
import org.herts.common.context.HertsMetricsSetting;
import org.herts.example.http.HttpServiceImpl02;
import org.herts.http.engine.HertsHttpEngine;
import org.herts.http.engine.HertsHttpServer;

public class HttpServer {

    public static void run() {
        var metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();

        HertsHttpEngine engine = HertsHttpServer.builder()
                .registerHertsHttpService(new HttpServiceImpl01(), new HttpServerInterceptor())
                .registerHertsHttpService(new HttpServiceImpl02())
                .setMetricsSetting(metrics)
                .setPort(Constant.port)
                .build();

        engine.start();
    }
}
