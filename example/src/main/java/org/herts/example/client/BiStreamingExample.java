package org.herts.example.client;

import org.herts.example.BidirectionalStreamingRpcRpcService;
import org.herts.example.BidirectionalStreamingRpcRpcServiceImpl;
import org.herts.example.HelloResponse;
import org.herts.common.context.HertsType;
import org.herts.common.logger.HertsLogger;
import org.herts.rpcclient.HertsRpcClient;
import org.herts.rpcclient.HertsRpcClientBuilder;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class BiStreamingExample {
    private static final Logger logger = HertsLogger.getLogger(BiStreamingExample.class.getSimpleName());

    public static void run() {
        BidirectionalStreamingRpcRpcService bidirectionalStreamingRpcRpcService = new BidirectionalStreamingRpcRpcServiceImpl();

        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost", 9000, HertsType.BidirectionalStreaming)
                .secure(false)
                .hertsImplementationService(bidirectionalStreamingRpcRpcService)
                .build();

        BidirectionalStreamingRpcRpcService service = client.createHertCoreInterface(BidirectionalStreamingRpcRpcService.class);
        var res1 = service.test04(new StreamObserver<>() {
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
        res1.onNext(r);
        res1.onCompleted();
    }
}
