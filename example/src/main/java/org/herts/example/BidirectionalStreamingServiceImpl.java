package org.herts.example;

import org.herts.common.logger.HertsLogger;
import org.herts.common.util.DateTimeUtil;
import org.herts.common.service.BidirectionalStreamingService;

import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class BidirectionalStreamingServiceImpl extends BidirectionalStreamingService<BidirectionalStreamingRpcService> implements BidirectionalStreamingRpcService {
    private static final Logger logger = HertsLogger.getLogger(BidirectionalStreamingServiceImpl.class.getSimpleName());

    public BidirectionalStreamingServiceImpl() {
    }

    public StreamObserver<HelloResponse> test04(final StreamObserver<HelloResponse> responseObserver) {
        logger.info("------------ BidirectionalStreaming test04 RPC");
        return new StreamObserver<>() {
            @Override
            public void onNext(HelloResponse response) {
                var req = new HelloResponse();
                req.setCode(99);
                req.setTimestamp(DateTimeUtil.getCurrentTimeMilliSec());
                responseObserver.onNext(req);
                logger.info("Caught from client. " + response.getCode());
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
