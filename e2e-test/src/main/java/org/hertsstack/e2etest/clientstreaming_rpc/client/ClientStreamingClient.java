package org.hertsstack.e2etest.clientstreaming_rpc.client;

import org.hertsstack.e2etest.common.Constant;
import org.hertsstack.e2etest.common.GrpcClientInterceptor;
import org.hertsstack.e2etest.common.HelloRequest;
import org.hertsstack.e2etest.common.HelloResponse01;
import org.hertsstack.e2etest.clientstreaming_rpc.ClientStreamingRpcService;
import org.hertsstack.core.logger.Logging;
import org.hertsstack.rpcclient.HertsRpcClient;
import org.hertsstack.rpcclient.HertsRpcClientBuilder;
import org.hertsstack.rpcclient.HertsRpcClientInterceptBuilder;

import io.grpc.stub.StreamObserver;

public class ClientStreamingClient {
    private static final java.util.logging.Logger logger = Logging.getLogger(ClientStreamingClient.class.getSimpleName());

    public static void run() {
        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost", Constant.port)
                .secure(false)
                .registerHertsRpcServiceInterface(ClientStreamingRpcService.class)
                .interceptor(HertsRpcClientInterceptBuilder.builder(new GrpcClientInterceptor()).build())
                .connect();

        ClientStreamingRpcService service = client.createHertsRpcService(ClientStreamingRpcService.class);
        StreamObserver<HelloRequest> res = service.test10(new StreamObserver<HelloResponse01>() {
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
            HelloRequest r = new HelloRequest();
            r.setNumber(10000);
            res.onNext(r);
        }

        res.onCompleted();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("Done");
        client.getChannel().shutdown();
    }
}
