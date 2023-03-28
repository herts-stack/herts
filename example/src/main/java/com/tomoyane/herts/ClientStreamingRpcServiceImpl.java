package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import com.tomoyane.herts.hertscore.ClientStreamingServiceCore;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class ClientStreamingRpcServiceImpl extends ClientStreamingServiceCore implements ClientStreamingRpcService {
    private static final Logger logger = HertsLogger.getLogger(ClientStreamingRpcServiceImpl.class.getSimpleName());

    public ClientStreamingRpcServiceImpl() {
    }

    @Override
    public StreamObserver<HelloRequest> test10(StreamObserver<HelloResponse> responseObserver) {
        logger.info("------------ test10 RPC ----------- ");
        return new StreamObserver<>() {
            @Override
            public void onNext(HelloRequest response) {
                logger.info("------------ " + response.getNumber());
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
