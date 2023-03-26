package com.tomoyane.herts;

import com.tomoyane.herts.hertsclient.HertsClient;
import com.tomoyane.herts.hertsclient.HertsClientBuilderImpl;
import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ClientServiceExample {
    private static final Logger logger = HertsLogger.getLogger(ClientServiceExample.class.getSimpleName());

    public static void unary() throws InterruptedException {
        UnaryRpcService01 service01 = new UnaryRpcServiceImpl01();
        UnaryRpcService02 service02 = new UnaryRpcServiceImpl02();

        HertsClient client = HertsClientBuilderImpl.Builder
                .create("localhost", 9000, HertsCoreType.Unary)
                .secure(false)
                .hertsImplementationService(service01)
                .hertsImplementationService(service02)
                .interceptor(new GrpcClientInterceptor())
                .build();

        UnaryRpcService01 service_01 = client.createHertService(UnaryRpcService01.class);
        var res01 = service_01.test01("TEST01", "VALUE01");
        var res02 = service_01.test02();
        var res03 = service_01.test03();
        var res100 = service_01.test100(new HelloRequest());

        UnaryRpcService02 service_02 = client.createHertService(UnaryRpcService02.class);
        var res0201 = service_02.hello01("ID", "Hello!");

        logger.info(res01);
        logger.info("" + res02);
        logger.info(res03.get("Key"));
        logger.info("=====================");
        logger.info(res0201);

        client.getChannel().shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }

    public static void bidirectionalStreaming() {
        HertsClient client = HertsClientBuilderImpl.Builder
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
        HertsClient client = HertsClientBuilderImpl.Builder
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
        HertsClient client = HertsClientBuilderImpl.Builder
                .create("localhost", 9000, HertsCoreType.BidirectionalStreaming)
                .secure(false)
                .hertsImplementationService(new ClientStreamingRpcServiceImpl())
                .interceptor(new GrpcClientInterceptor())
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
