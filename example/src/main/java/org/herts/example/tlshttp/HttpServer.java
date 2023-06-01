package org.herts.example.tlshttp;

import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.herts.common.context.HertsMetricsSetting;
import org.herts.http.engine.HertsHttpEngine;
import org.herts.http.engine.HertsHttpServer;

public class HttpServer {
    private static final String keyStoreFilePath = "";
    private static final String trustStoreFilePath = "";
    private static final String keyStorePassword = "";

    public static void runServer() {
        var metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();

        HertsHttpEngine engine = HertsHttpServer.builder()
                .registerHertsHttpService(new HttpServiceImpl())
                .setMetricsSetting(metrics)
                .setSsl(getSslCtx(), 443)
                .build();

        engine.start();
    }

    private static SslContextFactory getSslCtx() {
        final SslContextFactory sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath(keyStoreFilePath);
        sslContextFactory.setKeyStorePassword(keyStorePassword);
        return sslContextFactory;
    }
}
