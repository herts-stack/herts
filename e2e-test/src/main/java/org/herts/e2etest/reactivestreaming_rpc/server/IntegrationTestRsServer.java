package org.herts.e2etest.reactivestreaming_rpc.server;

import io.grpc.ServerInterceptor;
import org.herts.common.context.HertsMetricsSetting;
import org.herts.e2etest.common.Constant;
import org.herts.e2etest.common.GrpcServerInterceptor;
import org.herts.e2etest.reactivestreaming_rpc.IntegrationTestRsService;
import org.herts.e2etest.reactivestreaming_rpc.IntegrationTestRsServiceImpl;
import org.herts.rpc.HertsRpcInterceptBuilder;
import org.herts.rpc.engine.HertsRpcServerEngine;
import org.herts.rpc.engine.HertsRpcServerEngineBuilder;
import org.herts.rpc.engine.HertsRpcServer;

public class IntegrationTestRsServer {

    public static void run() {
        HertsMetricsSetting metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();
        ServerInterceptor interceptor = HertsRpcInterceptBuilder.builder(new GrpcServerInterceptor()).build();
        IntegrationTestRsService service = new IntegrationTestRsServiceImpl();

        HertsRpcServer engineBuilder = HertsRpcServerEngineBuilder.builder(Constant.getGrpcServerOption())
                .registerHertsReactiveRpcService(service, interceptor)
                .enableMetrics(metrics);

        HertsRpcServerEngine engine = engineBuilder.build();
        engine.start();
    }
}
