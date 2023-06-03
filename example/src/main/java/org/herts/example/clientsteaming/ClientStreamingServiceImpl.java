package org.herts.example.clientsteaming;

import io.grpc.stub.StreamObserver;
import org.herts.common.service.HertsClientStreamingService;

public class ClientStreamingServiceImpl extends HertsClientStreamingService<ClientStreamingService> implements ClientStreamingService {
    @Override
    public StreamObserver<String> helloWorld(StreamObserver<String> streamObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(String value) {
                System.out.println("Received data on server: " + value);
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
                streamObserver.onNext("hello");
                streamObserver.onCompleted();
            }
        };
    }
}
