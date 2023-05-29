package org.herts.e2etest.bidstreaming_rpc;

import org.herts.common.logger.HertsLogger;
import org.herts.common.util.DateTimeUtil;
import org.herts.common.service.HertsBidirectionalStreamingService;

import io.grpc.stub.StreamObserver;
import org.herts.e2etest.common.HelloResponse01;

import java.util.logging.Logger;

public class BidirectionalStreamingServiceImpl extends HertsBidirectionalStreamingService<BidirectionalStreamingRpcService> implements BidirectionalStreamingRpcService {
    private static final Logger logger = HertsLogger.getLogger(BidirectionalStreamingServiceImpl.class.getSimpleName());

    public BidirectionalStreamingServiceImpl() {
    }

    public StreamObserver<HelloResponse01> test04(final StreamObserver<HelloResponse01> responseObserver) {
        logger.info("------------ BidirectionalStreaming test04 RPC");
        return new StreamObserver<>() {
            @Override
            public void onNext(HelloResponse01 response) {
                var req = new HelloResponse01();
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
