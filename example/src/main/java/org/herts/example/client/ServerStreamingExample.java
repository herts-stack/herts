package org.herts.example.client;

import org.herts.example.HelloRequest;
import org.herts.example.HelloResponse;
import org.herts.example.ServerStreamingRpcService;
import org.herts.common.logger.HertsLogger;
import org.herts.rpcclient.HertsRpcClient;
import org.herts.rpcclient.HertsRpcClientBuilder;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class ServerStreamingExample {
    private static final Logger logger = HertsLogger.getLogger(ServerStreamingExample.class.getSimpleName());

    public static void run() {
        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost", 9000)
                .secure(false)
                .registerHertsRpcInterface(ServerStreamingRpcService.class)
                .connect();

        ServerStreamingRpcService service = client.createHertRpcService(ServerStreamingRpcService.class);
        var req = new HelloRequest();
        req.setNumber(7777);
        service.test05("ABC_id", req, new StreamObserver<HelloResponse>() {
            @Override
            public void onNext(HelloResponse req) {
                logger.info(String.format("Got message at %d, %d", req.getCode(), req.getTimestamp()));
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
    }
}
