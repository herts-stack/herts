package org.herts.e2etest.bidstreaming_rpc.server;

import io.grpc.ServerInterceptor;
import org.herts.e2etest.bidstreaming_rpc.BidirectionalStreamingRpcService;
import org.herts.e2etest.bidstreaming_rpc.BidirectionalStreamingServiceImpl;
import org.herts.core.context.HertsMetricsSetting;
import org.herts.e2etest.common.Constant;
import org.herts.e2etest.common.GrpcServerInterceptor;
import org.herts.rpc.HertsRpcInterceptBuilder;
import org.herts.rpc.HertsRpcServerEngine;
import org.herts.rpc.HertsRpcServerEngineBuilder;
import org.herts.rpc.RpcServer;

public class BiStreamingServer {

    public static void run() {
        HertsMetricsSetting metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();
        ServerInterceptor interceptor = HertsRpcInterceptBuilder.builder(new GrpcServerInterceptor()).build();
        BidirectionalStreamingRpcService service = new BidirectionalStreamingServiceImpl();

        RpcServer engineBuilder = HertsRpcServerEngineBuilder.builder(Constant.getGrpcServerOption())
                .registerHertsRpcService(service, interceptor)
                .enableMetrics(metrics);

        HertsRpcServerEngine engine = engineBuilder.build();
        engine.start();
    }
}
