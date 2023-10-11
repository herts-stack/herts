package org.hertsstack.example.bidsteaming;

import io.grpc.stub.StreamObserver;
import org.hertsstack.core.service.HertsServiceBidirectionalStreaming;
import org.hertsstack.example.commonmodel.Hoo;

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

    @Override
    public StreamObserver<Hoo> hoo(StreamObserver<Hoo> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(Hoo value) {
                System.out.println("Received hoo data on server: " + value);
                responseObserver.onNext(new Hoo());
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
