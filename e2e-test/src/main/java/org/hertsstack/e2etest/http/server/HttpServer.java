package org.hertsstack.e2etest.http.server;

import org.hertsstack.e2etest.http.HttpServiceImpl01;
import org.hertsstack.e2etest.common.Constant;
import org.hertsstack.core.context.HertsMetricsSetting;
import org.hertsstack.example.codegents.HttpCodegenTestServiceImpl;
import org.hertsstack.http.HertsHttpEngine;
import org.hertsstack.http.HertsHttpServer;

public class HttpServer {

    public static void run() {
        HertsMetricsSetting metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();

        HertsHttpEngine engine = HertsHttpServer.builder()
                .registerHertsHttpService(new HttpServiceImpl01(), new HttpServerInterceptor())
                .registerHertsHttpService(new HttpCodegenTestServiceImpl())
                .setMetricsSetting(metrics)
                .setPort(Constant.port)
                .build();

        engine.start();
    }
}
