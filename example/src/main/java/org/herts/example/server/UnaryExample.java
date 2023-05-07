package org.herts.example.server;

import org.herts.example.UnaryRpcService01;
import org.herts.example.UnaryRpcService02;
import org.herts.example.UnaryServiceImpl01;
import org.herts.example.UnaryServiceImpl02;
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
        UnaryRpcService01 service01 = new UnaryServiceImpl01();
        UnaryRpcService02 service02 = new UnaryServiceImpl02();

        var engine = HertsRpcBuilder.builder()
                .registerHertsRpcService(service01, interceptor)
                .registerHertsRpcService(service02, interceptor)
                .enableMetrics(metrics)
                .build();

        engine.start();
    }
}
