package org.hertsstack.e2etest.serverstreaming_rpc.server;

import io.grpc.ServerInterceptor;
import org.hertsstack.e2etest.serverstreaming_rpc.ServerStreamingServiceImpl;
import org.hertsstack.e2etest.common.Constant;
import org.hertsstack.e2etest.serverstreaming_rpc.ServerStreamingRpcService;
import org.hertsstack.core.context.HertsMetricsSetting;
import org.hertsstack.e2etest.common.GrpcServerInterceptor;
import org.hertsstack.rpc.HertsRpcInterceptBuilder;
import org.hertsstack.rpc.HertsRpcServerEngine;
import org.hertsstack.rpc.HertsRpcServerEngineBuilder;
import org.hertsstack.rpc.RpcServer;

public class ServerStreamingServer {

    public static void run() {
        HertsMetricsSetting metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();
        ServerInterceptor interceptor = HertsRpcInterceptBuilder.builder(new GrpcServerInterceptor()).build();
        ServerStreamingRpcService service = new ServerStreamingServiceImpl();

        RpcServer engineBuilder = HertsRpcServerEngineBuilder.builder(Constant.getGrpcServerOption())
                .registerHertsRpcService(service, interceptor)
                .enableMetrics(metrics);

        HertsRpcServerEngine engine = engineBuilder.build();
        engine.start();
    }
}
