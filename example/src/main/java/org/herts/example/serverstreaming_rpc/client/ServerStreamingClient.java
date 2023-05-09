package org.herts.example.client;

import org.herts.example.HelloRequest;
import org.herts.example.HelloResponse01;
import org.herts.example.ServerStreamingRpcService;
import org.herts.common.logger.HertsLogger;
import org.herts.example.HelloResponse02;
import org.herts.rpcclient.HertsRpcClient;
import org.herts.rpcclient.HertsRpcClientBuilder;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class ServerStreamingExample {
    private static final Logger logger = HertsLogger.getLogger(ServerStreamingExample.class.getSimpleName());

    public static void run() {
        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost", 9999)
                .secure(false)
                .registerHertsRpcInterface(ServerStreamingRpcService.class)
                .connect();

        ServerStreamingRpcService service = client.createHertsRpcService(ServerStreamingRpcService.class);
        var req = new HelloRequest();
        req.setNumber(7777);
        service.test05("ABC_id", req, new StreamObserver<>() {
            @Override
            public void onNext(HelloResponse01 res) {
                logger.info(String.format("Got message at %d, %d", res.getCode(), res.getTimestamp()));
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                logger.info("ERRRR");
            }

            @Override
            public void onCompleted() {
                logger.info("onCompleted");
            }
        });

        service.test01("id", "no", new StreamObserver<HelloResponse02>() {
            @Override
            public void onNext(HelloResponse02 value) {
                System.err.println("recevice");
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                System.err.println("done");
            }
        });
    }
}
