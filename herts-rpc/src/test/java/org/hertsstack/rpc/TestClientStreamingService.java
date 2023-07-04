package org.hertsstack.rpc;

import io.grpc.stub.StreamObserver;
import org.hertsstack.core.service.HertsServiceClientStreaming;

public class TestClientStreamingService extends HertsServiceClientStreaming<TestClientStreamingService> {
    public StreamObserver<String> test01(final StreamObserver<String> responseObserver) {
        return new StreamObserver<String>() {
            @Override
            public void onNext(String value) {
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
            }
        };
    }

    public StreamObserver<String> test02(final StreamObserver<String> responseObserver) {
        return new StreamObserver<String>() {
            @Override
            public void onNext(String value) {
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
            }
        };
    }

    public StreamObserver<String> test03(final StreamObserver<String> responseObserver) {
        return new StreamObserver<String>() {
            @Override
            public void onNext(String value) {
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
            }
        };
    }
}
