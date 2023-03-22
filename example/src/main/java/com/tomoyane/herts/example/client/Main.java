package com.tomoyane.herts.example.client;

import com.tomoyane.herts.example.*;
import com.tomoyane.herts.hertsclient.HertsClient;
import com.tomoyane.herts.hertsclient.HertsClientImpl;
import com.tomoyane.herts.hertscommon.enums.HertsCoreType;
import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = HertsLogger.getLogger(Main.class.getSimpleName());

    private static void unary() throws InterruptedException {
        HertsClient client = new HertsClientImpl.Builder("localhost", "9000", HertsCoreType.Unary)
                .secure(false)
                .hertsService(new UnaryRpcServiceImpl())
                .build();

        var service = (UnaryRpcService) client.createHertService(UnaryRpcService.class);
        var res01 = service.test01("TEST01", "VALUE01");
        var res02 = service.test02();
        var res03 = service.test03();

        logger.info(res01);
        logger.info("" + res02);
        logger.info(res03.get("Key"));

        client.getChannel().shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }

    private static void streaming() {
        HertsClient client = new HertsClientImpl.Builder("localhost", "9000", HertsCoreType.BidirectionalStreaming)
                .secure(false)
                .hertsService(new StreamingRpcServiceImpl())
                .build();

        var service = (StreamingRpcService) client.createHertService(StreamingRpcService.class);
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

    public static void main(String[] args) throws InterruptedException {
        try {
            unary();
//            streaming();
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}