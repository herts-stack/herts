package org.herts.example.clientsteaming;

import io.grpc.stub.StreamObserver;
import org.herts.rpc.engine.HertsRpcServerEngine;
import org.herts.rpc.engine.HertsRpcServerEngineBuilder;
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
                .registerHertsRpcService(new ClientStreamingServiceImpl())
                .build();

        Thread t = new Thread(engine::start);
        t.start();
    }

    private static void startClient() {
        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost")
                .secure(false)
                .registerHertsRpcServiceInterface(ClientStreamingService.class)
                .connect();

        ClientStreamingService service = client.createHertsRpcService(ClientStreamingService.class);
        var res = service.helloWorld(new StreamObserver<String>() {
            @Override
            public void onNext(String value) {
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
            }
        });

        for (var i = 0;i < 10; i++) {
            res.onNext("hello world " + i);
        }
        res.onCompleted();
    }
}