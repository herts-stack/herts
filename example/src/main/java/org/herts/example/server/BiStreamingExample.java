package org.herts.example.server;

import org.herts.example.BidirectionalStreamingRpcRpcServiceImpl;
import org.herts.example.GrpcServerInterceptor;
import org.herts.common.context.HertsMetricsSetting;
import org.herts.rpc.HertsRpcInterceptBuilder;
import org.herts.rpc.engine.HertsRpcBuilder;
import org.herts.rpc.engine.HertsRpcEngineBuilder;

public class BiStreamingExample {

    public static void run() {
        var metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();
        var interceptor = HertsRpcInterceptBuilder.builder(new GrpcServerInterceptor()).build();
        var service = new BidirectionalStreamingRpcRpcServiceImpl();

        HertsRpcEngineBuilder engineBuilder = HertsRpcBuilder.builder()
                .addService(service, interceptor)
                .enableMetrics(metrics);

        var engine = engineBuilder.build();
        engine.start();
    }
}
