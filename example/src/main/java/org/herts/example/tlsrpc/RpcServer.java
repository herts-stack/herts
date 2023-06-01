package org.herts.example.tlsrpc;

import org.herts.common.context.HertsMetricsSetting;
import org.herts.rpc.engine.HertsRpcServerEngine;
import org.herts.rpc.engine.HertsRpcServerEngineBuilder;

public class RpcServer {
    public static void runServer() {
        var metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();

        HertsRpcServerEngine engine = HertsRpcServerEngineBuilder.builder()
                .registerHertsRpcService(new RpcServiceImpl())
                .enableMetrics(metrics)
                .build();

        engine.start();
    }
}
