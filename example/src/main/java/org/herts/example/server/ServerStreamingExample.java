package org.herts.example.server;

import org.herts.example.ServerStreamingServiceImpl;
import org.herts.common.context.HertsMetricsSetting;
import org.herts.rpc.HertsRpcInterceptBuilder;
import org.herts.rpc.engine.HertsRpcBuilder;
import org.herts.rpc.engine.HertsRpcEngineBuilder;

public class ServerStreamingExample {

    public static void run() {
        var metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();
        var interceptor = HertsRpcInterceptBuilder.builder(new GrpcServerInterceptor()).build();
        var service = new ServerStreamingServiceImpl();

        HertsRpcEngineBuilder engineBuilder = HertsRpcBuilder.builder()
                .registerHertsRpcService(service, interceptor)
                .enableMetrics(metrics);

        var engine = engineBuilder.build();
        engine.start();
    }
}
