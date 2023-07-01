package org.hertsstack.example.serversteaming;

import io.grpc.stub.StreamObserver;
import org.hertsstack.core.service.HertsServiceServerStreaming;

public class ServerStreamingServiceImpl extends HertsServiceServerStreaming<ServerStreamingService> implements ServerStreamingService {
    @Override
    public void helloWorld(StreamObserver<String> responseObserver) {
        for (var i = 0; i < 10; i++) {
            responseObserver.onNext("hello world " + i);
        }
        responseObserver.onCompleted();
    }
}
