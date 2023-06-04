package org.herts.e2etest.reactivestreaming_rpc.server;

import org.herts.common.context.HertsMetricsSetting;
import org.herts.e2etest.common.Constant;
import org.herts.e2etest.common.GrpcServerInterceptor;
import org.herts.e2etest.reactivestreaming_rpc.IntegrationTestRsServiceImpl;
import org.herts.rpc.HertsRpcInterceptBuilder;
import org.herts.rpc.engine.HertsRpcServerEngineBuilder;
import org.herts.rpc.engine.HertsRpcServer;

public class IntegrationTestRsServer {

    public static void run() {
        var metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();
        var interceptor = HertsRpcInterceptBuilder.builder(new GrpcServerInterceptor()).build();
        var service = new IntegrationTestRsServiceImpl();

        HertsRpcServer engineBuilder = HertsRpcServerEngineBuilder.builder(Constant.getGrpcServerOption())
                .registerHertsReactiveRpcService(service, interceptor)
                .enableMetrics(metrics);

        var engine = engineBuilder.build();
        engine.start();
    }
}
