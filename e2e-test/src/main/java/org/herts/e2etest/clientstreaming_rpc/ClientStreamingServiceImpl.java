package org.herts.e2etest.clientstreaming_rpc;

import org.herts.core.logger.HertsLogger;
import org.herts.core.service.HertsServiceClientStreaming;
import io.grpc.stub.StreamObserver;
import org.herts.e2etest.common.HelloRequest;
import org.herts.e2etest.common.HelloResponse01;

import java.util.logging.Logger;

public class ClientStreamingServiceImpl extends HertsServiceClientStreaming<ClientStreamingRpcService> implements ClientStreamingRpcService {
    private static final Logger logger = HertsLogger.getLogger(ClientStreamingServiceImpl.class.getSimpleName());

    public ClientStreamingServiceImpl() {
    }

    @Override
    public StreamObserver<HelloRequest> test10(StreamObserver<HelloResponse01> responseObserver) {
        logger.info("------------ ClientStreaming test10 RPC");
        return new StreamObserver<HelloRequest>() {
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
                responseObserver.onNext(new HelloResponse01());
                responseObserver.onCompleted();
            }
        };
    }
}
