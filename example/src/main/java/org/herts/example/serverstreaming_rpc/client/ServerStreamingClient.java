package org.herts.example.serverstreaming_rpc.client;

import org.herts.example.common.Constant;
import org.herts.example.common.HelloRequest;
import org.herts.example.common.HelloResponse01;
import org.herts.example.serverstreaming_rpc.ServerStreamingRpcService;
import org.herts.common.logger.HertsLogger;
import org.herts.example.common.HelloResponse02;
import org.herts.rpcclient.HertsRpcClient;
import org.herts.rpcclient.HertsRpcClientBuilder;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class ServerStreamingClient {
    private static final Logger logger = HertsLogger.getLogger(ServerStreamingClient.class.getSimpleName());

    public static void run() {
        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost", Constant.port)
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
                logger.info("ERROR");
            }

            @Override
            public void onCompleted() {
                logger.info("onCompleted");
            }
        });

        service.test01("id", "no", new StreamObserver<HelloResponse02>() {
            @Override
            public void onNext(HelloResponse02 value) {
                logger.info("recevice");
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                logger.info("done");
            }
        });
    }
}
