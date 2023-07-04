package org.hertsstack.e2etest.clientstreaming_rpc.server;

import io.grpc.ServerInterceptor;
import org.hertsstack.e2etest.clientstreaming_rpc.ClientStreamingServiceImpl;
import org.hertsstack.e2etest.common.Constant;
import org.hertsstack.e2etest.common.GrpcServerInterceptor;
import org.hertsstack.e2etest.clientstreaming_rpc.ClientStreamingRpcService;
import org.hertsstack.core.context.HertsMetricsSetting;
import org.hertsstack.rpc.HertsRpcInterceptBuilder;
import org.hertsstack.rpc.HertsRpcServerEngine;
import org.hertsstack.rpc.HertsRpcServerEngineBuilder;
import org.hertsstack.rpc.RpcServer;

public class ClientStreamingServer {

    public static void run() {
        HertsMetricsSetting metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();
        ServerInterceptor interceptor = HertsRpcInterceptBuilder.builder(new GrpcServerInterceptor()).build();
        ClientStreamingRpcService service = new ClientStreamingServiceImpl();

        RpcServer engineBuilder = HertsRpcServerEngineBuilder.builder(Constant.getGrpcServerOption())
                .registerHertsRpcService(service, interceptor)
                .enableMetrics(metrics);

        HertsRpcServerEngine engine = engineBuilder.build();
        engine.start();
    }
}
