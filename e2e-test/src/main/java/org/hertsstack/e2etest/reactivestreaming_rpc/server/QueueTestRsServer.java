package org.hertsstack.e2etest.reactivestreaming_rpc.server;

import io.grpc.ServerInterceptor;
import org.hertsstack.broker.ReactiveBroker;
import org.hertsstack.brokerlocal.ConcurrentLocalBroker;
import org.hertsstack.core.context.HertsMetricsSetting;
import org.hertsstack.e2etest.reactivestreaming_rpc.QueueTestRsService;
import org.hertsstack.e2etest.reactivestreaming_rpc.QueueTestRsServiceImpl;
import org.hertsstack.e2etest.common.Constant;
import org.hertsstack.e2etest.common.GrpcServerInterceptor;
import org.hertsstack.rpc.HertsRpcInterceptBuilder;
import org.hertsstack.rpc.HertsRpcServerEngine;
import org.hertsstack.rpc.HertsRpcServerEngineBuilder;
import org.hertsstack.rpc.RpcServer;

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
