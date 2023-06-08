package org.herts.e2etest.http.server;

import org.herts.e2etest.common.Constant;
import org.herts.e2etest.http.HttpServiceImpl01;
import org.herts.core.context.HertsMetricsSetting;
import org.herts.e2etest.http.HttpServiceImpl02;
import org.herts.http.HertsHttpEngine;
import org.herts.http.HertsHttpServer;

public class HttpServer {

    public static void run() {
        HertsMetricsSetting metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();

        HertsHttpEngine engine = HertsHttpServer.builder()
                .registerHertsHttpService(new HttpServiceImpl01(), new HttpServerInterceptor())
                .registerHertsHttpService(new HttpServiceImpl02())
                .setMetricsSetting(metrics)
                .setPort(Constant.port)
                .build();

        engine.start();
    }
}
