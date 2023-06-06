package org.herts.e2etest.reactivestreaming_rpc.server;

import io.grpc.ServerInterceptor;
import org.herts.common.context.HertsMetricsSetting;
import org.herts.e2etest.common.Constant;
import org.herts.e2etest.common.GrpcServerInterceptor;
import org.herts.e2etest.reactivestreaming_rpc.QueueTestRsService;
import org.herts.e2etest.reactivestreaming_rpc.QueueTestRsServiceImpl;
import org.herts.rpc.HertsRpcInterceptBuilder;
import org.herts.rpc.engine.HertsRpcServerEngine;
import org.herts.rpc.engine.HertsRpcServerEngineBuilder;
import org.herts.rpc.engine.HertsRpcServer;

public class QueueTestRsServer {

    public static void run() {
        HertsMetricsSetting metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();
        ServerInterceptor interceptor = HertsRpcInterceptBuilder.builder(new GrpcServerInterceptor()).build();
        QueueTestRsService service = new QueueTestRsServiceImpl();

        HertsRpcServer engineBuilder = HertsRpcServerEngineBuilder.builder(Constant.getGrpcServerOption())
                .registerHertsReactiveRpcService(service, interceptor)
                .enableMetrics(metrics);

        HertsRpcServerEngine engine = engineBuilder.build();
        engine.start();
    }
}
