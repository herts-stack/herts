package org.hertsstack.example.reactivesteaming.redis;

import org.hertsstack.brokerredis.RedisBroker;
import org.hertsstack.example.reactivesteaming.ReactiveReceiverImpl;
import org.hertsstack.example.reactivesteaming.ReactiveService;
import org.hertsstack.example.reactivesteaming.ReactiveServiceImpl;
import org.hertsstack.rpc.HertsRpcServerEngine;
import org.hertsstack.rpc.HertsRpcServerEngineBuilder;
import org.hertsstack.rpcclient.HertsRpcClient;
import org.hertsstack.rpcclient.HertsRpcClientBuilder;
import redis.clients.jedis.JedisPool;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        startServer();
        startClient();
        Thread.sleep(2000);
        System.exit(0);
    }

    private static void startServer() {
        JedisPool pool = new JedisPool("localhost", 6379);

        HertsRpcServerEngine engine = HertsRpcServerEngineBuilder.builder()
                .registerHertsReactiveRpcService(new ReactiveServiceImpl())
                .loadBalancingBroker(RedisBroker.create(pool))
                .build();

        Thread t = new Thread(engine::start);
        t.start();
    }

    private static void startClient() {
        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost")
                .secure(false)
                .registerHertsRpcServiceInterface(ReactiveService.class)
                .registerHertsRpcReceiver(new ReactiveReceiverImpl())
                .connect();

        ReactiveService service = client.createHertsRpcService(ReactiveService.class);
        var res = service.helloWorld("hello");
        System.out.println("Received data on client: " + res);
    }
}