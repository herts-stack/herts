package org.hertsstack.e2etest.clientstreaming_rpc.client;

import io.grpc.stub.StreamObserver;
import org.hertsstack.core.logger.Logging;
import org.hertsstack.e2etest.clientstreaming_rpc.ClientStreamingRpcService;
import org.hertsstack.e2etest.common.Constant;
import org.hertsstack.e2etest.common.GrpcClientInterceptor;
import org.hertsstack.e2etest.common.HelloRequest;
import org.hertsstack.e2etest.common.HelloResponse01;
import org.hertsstack.rpcclient.HertsRpcClient;
import org.hertsstack.rpcclient.HertsRpcClientBuilder;
import org.hertsstack.rpcclient.HertsRpcClientInterceptBuilder;

public class AutoReconnectClientStreamingClient {
    private static final java.util.logging.Logger logger = Logging.getLogger(AutoReconnectClientStreamingClient.class.getSimpleName());

    public static void run() {
        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost", Constant.port)
                .secure(false)
                .registerHertsRpcServiceInterface(ClientStreamingRpcService.class)
                .autoReconnection(true)
                .interceptor(HertsRpcClientInterceptBuilder.builder(new GrpcClientInterceptor()).build())
                .connect();

        ClientStreamingRpcService service = client.createHertsRpcService(ClientStreamingRpcService.class);
        StreamObserver<HelloRequest> res01 = service.test10(test10Request());

        for (int i = 0; i < 20; i++) {
            try {
                HelloRequest r = new HelloRequest();
                r.setNumber(i);
                res01.onNext(r);
            } catch (Exception ex) {
                logger.warning(ex.getMessage());
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        res01.onCompleted();

        StreamObserver<HelloRequest> res02 = service.test10(test10Request());
        HelloRequest r = new HelloRequest();
        r.setNumber(999);
        res02.onNext(r);
        res02.onCompleted();

        logger.info("Done");
        client.getChannel().shutdown();
    }

    private static StreamObserver<HelloResponse01> test10Request() {
        return new StreamObserver<HelloResponse01>() {
            @Override
            public void onNext(HelloResponse01 req) {
                logger.info(String.format("Got message at %d, %d", req.getCode(), req.getTimestamp()));
            }
            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                logger.warning("Error. "  + t.getMessage());
            }
            @Override
            public void onCompleted() {
                logger.info("onCompleted.");
            }
        };
    }
}
