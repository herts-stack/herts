package org.hertsstack.example.bidsteaming;

import io.grpc.stub.StreamObserver;
import org.hertsstack.example.commonmodel.Hoo;
import org.hertsstack.rpc.HertsRpcServerEngine;
import org.hertsstack.rpc.HertsRpcServerEngineBuilder;
import org.hertsstack.rpcclient.HertsRpcClient;
import org.hertsstack.rpcclient.HertsRpcClientBuilder;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        startServer();
        startClient();
        Thread.sleep(5000);
        System.exit(0);
    }

    private static void startServer() {
        HertsRpcServerEngine engine = HertsRpcServerEngineBuilder.builder()
                .registerHertsRpcService(new BidStreamingServiceImpl())
                .build();

        Thread t = new Thread(engine::start);
        t.start();
    }

    private static void startClient() {
        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost")
                .secure(false)
                .registerHertsRpcServiceInterface(BidStreamingService.class)
                .connect();

        BidStreamingService service = client.createHertsRpcService(BidStreamingService.class);
        var res01 = service.helloWorld(new StreamObserver<String>() {
            @Override
            public void onNext(String value) {
                System.out.println("Received data on client: " + value);
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
            }
        });

        for (var i = 0;i < 10; i++) {
            res01.onNext("hello from client " + i);
        }
        res01.onCompleted();

        var res02 = service.hoo(new StreamObserver<Hoo>() {
            @Override
            public void onNext(Hoo value) {
                System.out.println("Received hoo data on client: " + value);
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
            }
        });

        for (var i = 0;i < 10; i++) {
            res02.onNext(new Hoo());
        }
        res02.onCompleted();
    }
}