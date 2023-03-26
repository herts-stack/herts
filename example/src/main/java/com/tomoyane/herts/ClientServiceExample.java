package com.tomoyane.herts;

import com.tomoyane.herts.hertsclient.HertsClient;
import com.tomoyane.herts.hertsclient.HertsClientBuilder;
import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ClientServiceExample {
    private static final Logger logger = HertsLogger.getLogger(ClientServiceExample.class.getSimpleName());

    public static void unary() throws InterruptedException {
        HertsClient client = HertsClientBuilder.Builder
                .create("localhost", 9000, HertsCoreType.Unary)
                .secure(false)
                .hertsImplementationService(new UnaryRpcServiceImpl())
                .interceptor(new GrpcInterceptor())
                .build();

        UnaryRpcService service = client.createHertService(UnaryRpcService.class);
        var res01 = service.test01("TEST01", "VALUE01");
        var res02 = service.test02();
        var res03 = service.test03();
        var res100 = service.test100(new HelloRequest());

        logger.info(res01);
        logger.info("" + res02);
        logger.info(res03.get("Key"));

        client.getChannel().shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }

    public static void bidirectionalStreaming() {
        HertsClient client = HertsClientBuilder.Builder
                .create("localhost", 9000, HertsCoreType.BidirectionalStreaming)
                .secure(false)
                .hertsImplementationService(new BidirectionalStreamingRpcServiceImpl())
                .build();

        BidirectionalStreamingRpcService service = client.createHertService(BidirectionalStreamingRpcService.class);
        var res = service.test04(new StreamObserver<>() {
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

        var r = new HelloResponse();
        r.setCode(10000);
        res.onNext(r);
        res.onCompleted();
    }

    public static void serverStreaming() {
        HertsClient client = HertsClientBuilder.Builder
                .create("localhost", 9000, HertsCoreType.ServerStreaming)
                .secure(false)
                .hertsImplementationService(new ServerStreamingRpcServiceImpl())
                .build();

        ServerStreamingRpcService service = client.createHertService(ServerStreamingRpcService.class);
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

    public static void clientStreaming() {
        HertsClient client = HertsClientBuilder.Builder
                .create("localhost", 9000, HertsCoreType.BidirectionalStreaming)
                .secure(false)
                .hertsImplementationService(new ClientStreamingRpcServiceImpl())
                .interceptor(new GrpcInterceptor())
                .build();

        ClientStreamingRpcService service = client.createHertService(ClientStreamingRpcService.class);
        var res = service.test10(new StreamObserver<>() {
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

        for (int i = 0; i < 10; i++) {
            var r = new HelloRequest();
            r.setNumber(10000);
            res.onNext(r);
        }
        res.onCompleted();
    }

}
