package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import com.tomoyane.herts.hertscommon.util.DateTimeUtil;
import com.tomoyane.herts.hertscommon.service.BidirectionalStreamingRpcServiceRpc;

import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class BidirectionalStreamingRpcRpcServiceImpl extends BidirectionalStreamingRpcServiceRpc implements BidirectionalStreamingRpcRpcService {
    private static final Logger logger = HertsLogger.getLogger(BidirectionalStreamingRpcRpcServiceImpl.class.getSimpleName());

    public BidirectionalStreamingRpcRpcServiceImpl() {
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
