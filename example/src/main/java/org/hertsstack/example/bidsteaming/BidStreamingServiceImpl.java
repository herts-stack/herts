package org.hertsstack.example.bidsteaming;

import io.grpc.stub.StreamObserver;
import org.hertsstack.core.service.HertsServiceBidirectionalStreaming;

public class BidStreamingServiceImpl extends HertsServiceBidirectionalStreaming<BidStreamingService> implements BidStreamingService {
    @Override
    public StreamObserver<String> helloWorld(StreamObserver<String> streamObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(String value) {
                System.out.println("Received data on server: " + value);
                streamObserver.onNext("hello from server");
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
                streamObserver.onCompleted();
            }
        };
    }
}
