package org.herts.e2etest.unary_rpc.server;

import io.grpc.ServerInterceptor;
import org.herts.e2etest.common.Constant;
import org.herts.e2etest.common.GrpcServerInterceptor;
import org.herts.e2etest.unary_rpc.UnaryRpcService01;
import org.herts.e2etest.unary_rpc.UnaryRpcService02;
import org.herts.e2etest.unary_rpc.UnaryServiceImpl01;
import org.herts.e2etest.unary_rpc.UnaryServiceImpl02;
import org.herts.core.context.HertsMetricsSetting;
import org.herts.rpc.HertsRpcInterceptBuilder;
import org.herts.rpc.HertsRpcServerEngine;
import org.herts.rpc.HertsRpcServerEngineBuilder;

public class UnaryServer {
    public static void run() {
        HertsMetricsSetting metrics = HertsMetricsSetting.builder()
                .isRpsEnabled(true)
                .isLatencyEnabled(true)
                .build();

        ServerInterceptor interceptor = HertsRpcInterceptBuilder.builder(new GrpcServerInterceptor()).build();
        UnaryRpcService01 service01 = new UnaryServiceImpl01();
        UnaryRpcService02 service02 = new UnaryServiceImpl02();

        HertsRpcServerEngine engine = HertsRpcServerEngineBuilder.builder(Constant.getGrpcServerOption())
                .registerHertsRpcService(service01, interceptor)
                .registerHertsRpcService(service02, interceptor)
                .enableMetrics(metrics)
                .build();

        engine.start();
    }
}
