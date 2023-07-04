package org.hertsstack.example.unary;

import org.hertsstack.rpc.HertsRpcServerEngine;
import org.hertsstack.rpc.HertsRpcServerEngineBuilder;
import org.hertsstack.rpcclient.HertsRpcClient;
import org.hertsstack.rpcclient.HertsRpcClientBuilder;

public class Main {
    public static void main(String[] args) {
        startServer();
        startClient();
        System.exit(0);
    }

    private static void startServer() {
        HertsRpcServerEngine engine = HertsRpcServerEngineBuilder.builder()
                .registerHertsRpcService(new UnaryServiceImpl())
                .build();

        Thread t = new Thread(engine::start);
        t.start();
    }

    private static void startClient() {
        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost")
                .secure(false)
                .registerHertsRpcServiceInterface(UnaryService.class)
                .connect();

        UnaryService service = client.createHertsRpcService(UnaryService.class);
        var res = service.helloWorld();
        System.out.println(res);
    }

}