package org.herts.example.bidstreaming_rpc.server;

import org.herts.example.bidstreaming_rpc.BidirectionalStreamingServiceImpl;
import org.herts.common.context.HertsMetricsSetting;
import org.herts.example.common.Constant;
import org.herts.example.common.GrpcServerInterceptor;
import org.herts.rpc.HertsRpcInterceptBuilder;
import org.herts.rpc.engine.HertsRpcBuilder;
import org.herts.rpc.engine.HertsRpcEngineBuilder;

public class BiStreamingServer {

    public static void run() {
        var metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();
        var interceptor = HertsRpcInterceptBuilder.builder(new GrpcServerInterceptor()).build();
        var service = new BidirectionalStreamingServiceImpl();

        HertsRpcEngineBuilder engineBuilder = HertsRpcBuilder.builder(Constant.getGrpcServerOption())
                .registerHertsRpcService(service, interceptor)
                .enableMetrics(metrics);

        var engine = engineBuilder.build();
        engine.start();
    }
}
