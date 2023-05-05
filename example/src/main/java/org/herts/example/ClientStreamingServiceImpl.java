package org.herts.example;

import org.herts.common.logger.HertsLogger;
import org.herts.common.service.HertsClientStreamingService;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class ClientStreamingServiceImpl extends HertsClientStreamingService<ClientStreamingRpcService> implements ClientStreamingRpcService {
    private static final Logger logger = HertsLogger.getLogger(ClientStreamingServiceImpl.class.getSimpleName());

    public ClientStreamingServiceImpl() {
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
