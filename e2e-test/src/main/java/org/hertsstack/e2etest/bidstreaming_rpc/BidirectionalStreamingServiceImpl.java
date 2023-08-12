package org.hertsstack.e2etest.bidstreaming_rpc;

import org.hertsstack.core.logger.Logging;
import org.hertsstack.core.util.DateTimeUtil;
import org.hertsstack.core.service.HertsServiceBidirectionalStreaming;

import io.grpc.stub.StreamObserver;
import org.hertsstack.e2etest.common.HelloResponse01;

public class BidirectionalStreamingServiceImpl extends HertsServiceBidirectionalStreaming<BidirectionalStreamingRpcService> implements BidirectionalStreamingRpcService {
    private static final java.util.logging.Logger logger = Logging.getLogger(BidirectionalStreamingServiceImpl.class.getSimpleName());

    public BidirectionalStreamingServiceImpl() {
    }

    public StreamObserver<HelloResponse01> test04(final StreamObserver<HelloResponse01> responseObserver) {
        logger.info("BidirectionalStreaming test04 RPC");
        return new StreamObserver<HelloResponse01>() {
            @Override
            public void onNext(HelloResponse01 response) {
                HelloResponse01 req = new HelloResponse01();
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
