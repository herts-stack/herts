package org.herts.e2etest.clientstreaming_rpc.server;

import org.herts.e2etest.clientstreaming_rpc.ClientStreamingServiceImpl;
import org.herts.common.context.HertsMetricsSetting;
import org.herts.e2etest.common.Constant;
import org.herts.e2etest.common.GrpcServerInterceptor;
import org.herts.rpc.HertsRpcInterceptBuilder;
import org.herts.rpc.engine.HertsRpcBuilder;
import org.herts.rpc.engine.HertsRpcEngineBuilder;

public class ClientStreamingServer {

    public static void run() {
        var metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();
        var interceptor = HertsRpcInterceptBuilder.builder(new GrpcServerInterceptor()).build();
        var service = new ClientStreamingServiceImpl();

        HertsRpcEngineBuilder engineBuilder = HertsRpcBuilder.builder(Constant.getGrpcServerOption())
                .registerHertsRpcService(service, interceptor)
                .enableMetrics(metrics);

        var engine = engineBuilder.build();
        engine.start();
    }
}
