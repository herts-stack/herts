package org.herts.example.client;

import org.herts.example.ClientStreamingRpcRpcService;
import org.herts.example.ClientStreamingRpcRpcServiceImpl;
import org.herts.example.GrpcClientInterceptor;
import org.herts.example.HelloRequest;
import org.herts.example.HelloResponse;
import org.herts.common.context.HertsType;
import org.herts.common.logger.HertsLogger;
import org.herts.rpcclient.HertsRpcClient;
import org.herts.rpcclient.HertsRpcClientBuilder;
import org.herts.rpcclient.HertsRpcClientInterceptBuilder;

import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class ClientStreamingExample {
    private static final Logger logger = HertsLogger.getLogger(ClientStreamingExample.class.getSimpleName());

    public static void run() {
        ClientStreamingRpcRpcService clientStreamingRpcRpcService = new ClientStreamingRpcRpcServiceImpl();

        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost", 9000, HertsType.ClientStreaming)
                .secure(false)
                .hertsImplementationService(clientStreamingRpcRpcService)
                .interceptor(HertsRpcClientInterceptBuilder.builder(new GrpcClientInterceptor()).build())
                .build();

        ClientStreamingRpcRpcService service = client.createHertCoreInterface(ClientStreamingRpcRpcService.class);
        var res = service.test10(new StreamObserver<>() {
            @Override
            public void onNext(HelloResponse req) {
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
