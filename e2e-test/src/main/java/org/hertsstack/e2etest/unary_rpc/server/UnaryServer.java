package org.hertsstack.e2etest.unary_rpc.server;

import io.grpc.ServerInterceptor;
import org.hertsstack.e2etest.common.Constant;
import org.hertsstack.e2etest.common.GrpcServerInterceptor;
import org.hertsstack.e2etest.unary_rpc.UnaryRpcService01;
import org.hertsstack.e2etest.unary_rpc.UnaryRpcService02;
import org.hertsstack.e2etest.unary_rpc.UnaryServiceImpl01;
import org.hertsstack.e2etest.unary_rpc.UnaryServiceImpl02;
import org.hertsstack.core.context.HertsMetricsSetting;
import org.hertsstack.rpc.HertsRpcInterceptBuilder;
import org.hertsstack.rpc.HertsRpcServerEngine;
import org.hertsstack.rpc.HertsRpcServerEngineBuilder;

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
