package org.herts.e2etest.clientstreaming_rpc.client;

import org.herts.e2etest.clientstreaming_rpc.ClientStreamingRpcService;
import org.herts.e2etest.common.Constant;
import org.herts.e2etest.common.HelloRequest;
import org.herts.e2etest.common.HelloResponse01;
import org.herts.common.logger.HertsLogger;
import org.herts.e2etest.common.GrpcClientInterceptor;
import org.herts.rpcclient.HertsRpcClient;
import org.herts.rpcclient.HertsRpcClientBuilder;
import org.herts.rpcclient.HertsRpcClientInterceptBuilder;

import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class ClientStreamingClient {
    private static final Logger logger = HertsLogger.getLogger(ClientStreamingClient.class.getSimpleName());

    public static void run() {
        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost", Constant.port)
                .secure(false)
                .registerHertsRpcServiceInterface(ClientStreamingRpcService.class)
                .interceptor(HertsRpcClientInterceptBuilder.builder(new GrpcClientInterceptor()).build())
                .connect();

        ClientStreamingRpcService service = client.createHertsRpcService(ClientStreamingRpcService.class);
        var res = service.test10(new StreamObserver<>() {
            @Override
            public void onNext(HelloResponse01 req) {
                logger.info(String.format("Got message at %d, %d", req.getCode(), req.getTimestamp()));
            }
            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                logger.info("Error. "  + t.getMessage());
            }
            @Override
            public void onCompleted() {
                logger.info("onCompleted.");
            }
        });

        for (int i = 0; i < 10; i++) {
            var r = new HelloRequest();
            r.setNumber(10000);
            res.onNext(r);
        }

        res.onCompleted();
        logger.info("Done");
        client.getChannel().shutdown();
    }

}
