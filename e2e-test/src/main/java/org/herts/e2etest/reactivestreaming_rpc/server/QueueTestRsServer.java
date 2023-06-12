package org.herts.e2etest.reactivestreaming_rpc.server;

import io.grpc.ServerInterceptor;
import org.herts.broker.ReactiveBroker;
import org.herts.brokerlocal.ConcurrentLocalBroker;
import org.herts.brokerredis.RedisBroker;
import org.herts.core.context.HertsMetricsSetting;
import org.herts.e2etest.common.Constant;
import org.herts.e2etest.common.GrpcServerInterceptor;
import org.herts.e2etest.reactivestreaming_rpc.QueueTestRsService;
import org.herts.e2etest.reactivestreaming_rpc.QueueTestRsServiceImpl;
import org.herts.rpc.HertsRpcInterceptBuilder;
import org.herts.rpc.HertsRpcServerEngine;
import org.herts.rpc.HertsRpcServerEngineBuilder;
import org.herts.rpc.RpcServer;
import redis.clients.jedis.JedisPool;

public class QueueTestRsServer {

    public static void run() {
        JedisPool pool = new JedisPool("localhost", 6379);
        ReactiveBroker localBroker = ConcurrentLocalBroker.getInstance();
        ReactiveBroker redisBroker = RedisBroker.create(pool);

        HertsMetricsSetting metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();
        ServerInterceptor interceptor = HertsRpcInterceptBuilder.builder(new GrpcServerInterceptor()).build();
        QueueTestRsService service = new QueueTestRsServiceImpl();

        RpcServer engineBuilder = HertsRpcServerEngineBuilder.builder(Constant.getGrpcServerOption())
                .registerHertsReactiveRpcService(service, interceptor)
                .loadBalancingBroker(redisBroker)
                .enableMetrics(metrics);

        HertsRpcServerEngine engine = engineBuilder.build();
        engine.start();
    }
}
