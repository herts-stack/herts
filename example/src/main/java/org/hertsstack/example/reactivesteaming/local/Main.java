package org.hertsstack.example.reactivesteaming.local;

import org.hertsstack.example.commonmodel.Hoo;
import org.hertsstack.example.reactivesteaming.ReactiveReceiverImpl;
import org.hertsstack.example.reactivesteaming.ReactiveService;
import org.hertsstack.example.reactivesteaming.ReactiveServiceImpl;
import org.hertsstack.rpc.HertsRpcServerEngine;
import org.hertsstack.rpc.HertsRpcServerEngineBuilder;
import org.hertsstack.rpcclient.HertsRpcClient;
import org.hertsstack.rpcclient.HertsRpcClientBuilder;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        startServer();
        startClient();
        Thread.sleep(2000);
        System.exit(0);
    }

    private static void startServer() {
        HertsRpcServerEngine engine = HertsRpcServerEngineBuilder.builder()
                .registerHertsReactiveRpcService(new ReactiveServiceImpl())
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

        Hoo hoo = service.getHoo();
        System.out.println(hoo);
    }
}