package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import com.tomoyane.herts.hertscommon.service.ClientStreamingCoreServiceCore;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class ClientStreamingRpcCoreServiceImpl extends ClientStreamingCoreServiceCore implements ClientStreamingRpcCoreService {
    private static final Logger logger = HertsLogger.getLogger(ClientStreamingRpcCoreServiceImpl.class.getSimpleName());

    public ClientStreamingRpcCoreServiceImpl() {
    }

    @Override
    public StreamObserver<HelloRequest> test10(StreamObserver<HelloResponse> responseObserver) {
        logger.info("------------ ClientStreaming test10 RPC");
        return new StreamObserver<>() {
            @Override
            public void onNext(HelloRequest response) {
                logger.info("Caught from client. " + response.getNumber());
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
                logger.info("Completed");
                responseObserver.onNext(new HelloResponse());
                responseObserver.onCompleted();
            }
        };
    }
}
