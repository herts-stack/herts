package org.herts.e2etest.clientstreaming_rpc.server;

import io.grpc.ServerInterceptor;
import org.herts.e2etest.clientstreaming_rpc.ClientStreamingRpcService;
import org.herts.e2etest.clientstreaming_rpc.ClientStreamingServiceImpl;
import org.herts.core.context.HertsMetricsSetting;
import org.herts.e2etest.common.Constant;
import org.herts.e2etest.common.GrpcServerInterceptor;
import org.herts.rpc.HertsRpcInterceptBuilder;
import org.herts.rpc.HertsRpcServerEngine;
import org.herts.rpc.HertsRpcServerEngineBuilder;
import org.herts.rpc.HertsRpcServer;

public class ClientStreamingServer {

    public static void run() {
        HertsMetricsSetting metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();
        ServerInterceptor interceptor = HertsRpcInterceptBuilder.builder(new GrpcServerInterceptor()).build();
        ClientStreamingRpcService service = new ClientStreamingServiceImpl();

        HertsRpcServer engineBuilder = HertsRpcServerEngineBuilder.builder(Constant.getGrpcServerOption())
                .registerHertsRpcService(service, interceptor)
                .enableMetrics(metrics);

        HertsRpcServerEngine engine = engineBuilder.build();
        engine.start();
    }
}
