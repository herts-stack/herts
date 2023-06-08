package org.herts.example.bidsteaming;

import io.grpc.stub.StreamObserver;
import org.herts.rpc.HertsRpcServerEngine;
import org.herts.rpc.HertsRpcServerEngineBuilder;
import org.herts.rpcclient.HertsRpcClient;
import org.herts.rpcclient.HertsRpcClientBuilder;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        startServer();
        startClient();
        Thread.sleep(2000);
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
        var res = service.helloWorld(new StreamObserver<String>() {
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
            res.onNext("hello from client " + i);
        }
    }
}