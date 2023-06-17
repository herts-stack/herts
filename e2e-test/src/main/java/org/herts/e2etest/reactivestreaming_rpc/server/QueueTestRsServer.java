package org.herts.e2etest.reactivestreaming_rpc.server;

import io.grpc.ServerInterceptor;
import org.herts.broker.ReactiveBroker;
import org.herts.brokerlocal.ConcurrentLocalBroker;
import org.herts.core.context.HertsMetricsSetting;
import org.herts.e2etest.common.Constant;
import org.herts.e2etest.common.GrpcServerInterceptor;
import org.herts.e2etest.reactivestreaming_rpc.QueueTestRsService;
import org.herts.e2etest.reactivestreaming_rpc.QueueTestRsServiceImpl;
import org.herts.rpc.HertsRpcInterceptBuilder;
import org.herts.rpc.HertsRpcServerEngine;
import org.herts.rpc.HertsRpcServerEngineBuilder;
import org.herts.rpc.RpcServer;

public class QueueTestRsServer {

    public static void run(String redisHost) {
//        redisHost = redisHost == null || redisHost.isEmpty() ? "10.244.0.5" : redisHost;
//        JedisPool pool = new JedisPool(redisHost, 6379);
//        ReactiveBroker redisBroker = RedisBroker.create(pool);
        ReactiveBroker localBroker = ConcurrentLocalBroker.getInstance();

        HertsMetricsSetting metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();
        ServerInterceptor interceptor = HertsRpcInterceptBuilder.builder(new GrpcServerInterceptor()).build();
        QueueTestRsService service = new QueueTestRsServiceImpl();

        RpcServer engineBuilder = HertsRpcServerEngineBuilder.builder(Constant.getGrpcServerOption())
                .registerHertsReactiveRpcService(service, interceptor)
                .loadBalancingBroker(localBroker)
                .enableMetrics(metrics);

        HertsRpcServerEngine engine = engineBuilder.build();
        engine.start();
    }
}
