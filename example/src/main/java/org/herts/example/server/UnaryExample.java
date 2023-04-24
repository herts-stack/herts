package org.herts.example.server;

import org.herts.example.GrpcServerInterceptor;
import org.herts.example.UnaryRpcRpcServiceImpl01;
import org.herts.example.UnaryRpcRpcServiceImpl02;
import org.herts.common.context.HertsMetricsSetting;
import org.herts.rpc.HertsRpcInterceptBuilder;
import org.herts.rpc.engine.HertsRpcBuilder;
import org.herts.rpc.engine.HertsRpcEngineBuilder;

public class UnaryExample {
    public static void run() {
        var metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();
        var interceptor = HertsRpcInterceptBuilder.builder(new GrpcServerInterceptor()).build();
        var service01 = new UnaryRpcRpcServiceImpl01();
        var service02 = new UnaryRpcRpcServiceImpl02();

        HertsRpcEngineBuilder engineBuilder = HertsRpcBuilder.builder()
                .addService(service01, interceptor)
                .addService(service02, interceptor)
                .enableMetrics(metrics);

        var engine = engineBuilder.build();
        engine.start();
    }
}
