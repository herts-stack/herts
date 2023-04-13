package com.tomoyane.herts;

import com.tomoyane.herts.hertscoreclient.HertCoreClientInterceptBuilder;
import com.tomoyane.herts.hertscoreclient.HertsCoreClient;
import com.tomoyane.herts.hertscoreclient.HertsCoreClientBuilder;
import com.tomoyane.herts.hertscommon.context.HertsType;
import com.tomoyane.herts.hertscommon.logger.HertsLogger;

import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ClientServiceExample {
    private static final Logger logger = HertsLogger.getLogger(ClientServiceExample.class.getSimpleName());

    public static void unary() throws InterruptedException {
        UnaryRpcCoreService01 service01 = new UnaryRpcCoreServiceImpl01();
        UnaryRpcCoreService02 service02 = new UnaryRpcCoreServiceImpl02();

        HertsCoreClient client = HertsCoreClientBuilder.Builder
                .create("localhost", 9000, HertsType.Unary)
                .secure(false)
                .hertsImplementationService(service01)
                .hertsImplementationService(service02)
                .interceptor(HertCoreClientInterceptBuilder.Builder.create(new GrpcClientInterceptor()).build())
                .build();

        UnaryRpcCoreService01 service_01 = client.createHertCoreInterface(UnaryRpcCoreService01.class);
        var res01 = service_01.test01("TEST01", "VALUE01");
        logger.info(res01);

        var res02 = service_01.test02();
        logger.info("" + res02);

        var res03 = service_01.test03();
        logger.info(res03.get("Key"));

        var res100 = service_01.test100(new HelloRequest());
        logger.info(""  + res100);

        UnaryRpcCoreService02 service_02 = client.createHertCoreInterface(UnaryRpcCoreService02.class);
        var res0201 = service_02.hello01("ID", "Hello!");
        logger.info(res0201);

        client.getChannel().shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }

    public static void bidirectionalStreaming() {
        HertsCoreClient client = HertsCoreClientBuilder.Builder
                .create("localhost", 9000, HertsType.BidirectionalStreaming)
                .secure(false)
                .hertsImplementationService(new BidirectionalStreamingRpcCoreServiceImpl())
                .build();

        BidirectionalStreamingRpcCoreService service = client.createHertCoreInterface(BidirectionalStreamingRpcCoreService.class);
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
        HertsCoreClient client = HertsCoreClientBuilder.Builder
                .create("localhost", 9000, HertsType.ServerStreaming)
                .secure(false)
                .hertsImplementationService(new ServerStreamingRpcCoreServiceImpl())
                .build();

        ServerStreamingRpcCoreService service = client.createHertCoreInterface(ServerStreamingRpcCoreService.class);
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
        HertsCoreClient client = HertsCoreClientBuilder.Builder
                .create("localhost", 9000, HertsType.ClientStreaming)
                .secure(false)
                .hertsImplementationService(new ClientStreamingRpcCoreServiceImpl())
                .interceptor(HertCoreClientInterceptBuilder.Builder.create(new GrpcClientInterceptor()).build())
                .build();

        ClientStreamingRpcCoreService service = client.createHertCoreInterface(ClientStreamingRpcCoreService.class);
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
        logger.info("DONE");
        client.getChannel().shutdown();
    }
}
