package org.herts.example.unary_rpc.server;

import org.herts.example.common.Constant;
import org.herts.example.common.GrpcServerInterceptor;
import org.herts.example.unary_rpc.UnaryRpcService01;
import org.herts.example.unary_rpc.UnaryRpcService02;
import org.herts.example.unary_rpc.UnaryServiceImpl01;
import org.herts.example.unary_rpc.UnaryServiceImpl02;
import org.herts.common.context.HertsMetricsSetting;
import org.herts.rpc.HertsRpcInterceptBuilder;
import org.herts.rpc.engine.GrpcServerOption;
import org.herts.rpc.engine.HertsRpcBuilder;

public class UnaryServer {
    public static void run() {
        var metrics = HertsMetricsSetting.builder()
                .isRpsEnabled(true)
                .isLatencyEnabled(true)
                .build();

        var interceptor = HertsRpcInterceptBuilder.builder(new GrpcServerInterceptor()).build();
        UnaryRpcService01 service01 = new UnaryServiceImpl01();
        UnaryRpcService02 service02 = new UnaryServiceImpl02();

        var engine = HertsRpcBuilder.builder(Constant.getGrpcServerOption())
                .registerHertsRpcService(service01, interceptor)
                .registerHertsRpcService(service02, interceptor)
                .enableMetrics(metrics)
                .build();

        engine.start();
    }
}
