package org.herts.example.bidsteaming;

import io.grpc.stub.StreamObserver;
import org.herts.common.service.HertsBidirectionalStreamingService;
import org.herts.common.service.HertsClientStreamingService;
import org.herts.example.clientsteaming.ClientStreamingService;

public class BidStreamingServiceImpl extends HertsBidirectionalStreamingService<BidStreamingService> implements BidStreamingService {
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
