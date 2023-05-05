package org.herts.example.server;

import org.herts.example.UnaryRpcRpcService01;
import org.herts.example.UnaryRpcRpcService02;
import org.herts.example.UnaryServiceServiceImpl01;
import org.herts.example.UnaryServiceServiceImpl02;
import org.herts.common.context.HertsMetricsSetting;
import org.herts.rpc.HertsRpcInterceptBuilder;
import org.herts.rpc.engine.HertsRpcBuilder;

public class UnaryExample {
    public static void run() {
        var metrics = HertsMetricsSetting.builder()
                .isRpsEnabled(true)
                .isLatencyEnabled(true)
                .build();

        var interceptor = HertsRpcInterceptBuilder.builder(new GrpcServerInterceptor()).build();
        UnaryRpcRpcService01 service01 = new UnaryServiceServiceImpl01();
        UnaryRpcRpcService02 service02 = new UnaryServiceServiceImpl02();

        var engine = HertsRpcBuilder.builder()
                .registerHertsRpcService(service01, interceptor)
                .registerHertsRpcService(service02, interceptor)
                .enableMetrics(metrics)
                .build();

        engine.start();
    }
}
