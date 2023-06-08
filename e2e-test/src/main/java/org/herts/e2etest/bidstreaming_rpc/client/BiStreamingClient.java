package org.herts.e2etest.bidstreaming_rpc.client;

import org.herts.e2etest.bidstreaming_rpc.BidirectionalStreamingRpcService;
import org.herts.e2etest.common.Constant;
import org.herts.e2etest.common.HelloResponse01;
import org.herts.core.logger.HertsLogger;
import org.herts.rpcclient.HertsRpcClient;
import org.herts.rpcclient.HertsRpcClientBuilder;

import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class BiStreamingClient {
    private static final Logger logger = HertsLogger.getLogger(BiStreamingClient.class.getSimpleName());

    public static void run() {

        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost", Constant.port)
                .secure(false)
                .registerHertsRpcServiceInterface(BidirectionalStreamingRpcService.class)
                .connect();

        BidirectionalStreamingRpcService service = client.createHertsRpcService(BidirectionalStreamingRpcService.class);
        StreamObserver<HelloResponse01> res1 = service.test04(new StreamObserver<HelloResponse01>() {
            @Override
            public void onNext(HelloResponse01 req) {
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

        HelloResponse01 r = new HelloResponse01();
        r.setCode(10000);
        res1.onNext(r);
        res1.onCompleted();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
