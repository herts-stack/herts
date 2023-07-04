package org.hertsstack.e2etest.reactivestreaming_rpc.server;

import io.grpc.ServerInterceptor;
import org.hertsstack.core.context.HertsMetricsSetting;
import org.hertsstack.e2etest.reactivestreaming_rpc.IntegrationTestRsService;
import org.hertsstack.e2etest.reactivestreaming_rpc.IntegrationTestRsServiceImpl;
import org.hertsstack.e2etest.common.Constant;
import org.hertsstack.e2etest.common.GrpcServerInterceptor;
import org.hertsstack.rpc.HertsRpcInterceptBuilder;
import org.hertsstack.rpc.HertsRpcServerEngine;
import org.hertsstack.rpc.HertsRpcServerEngineBuilder;
import org.hertsstack.rpc.RpcServer;

public class IntegrationTestRsServer {

    public static void run() {
        HertsMetricsSetting metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();
        ServerInterceptor interceptor = HertsRpcInterceptBuilder.builder(new GrpcServerInterceptor()).build();
        IntegrationTestRsService service = new IntegrationTestRsServiceImpl();

        RpcServer engineBuilder = HertsRpcServerEngineBuilder.builder(Constant.getGrpcServerOption())
                .registerHertsReactiveRpcService(service, interceptor)
                .enableMetrics(metrics);

        HertsRpcServerEngine engine = engineBuilder.build();
        engine.start();
    }
}
