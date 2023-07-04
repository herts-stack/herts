package org.herts.rpc;

import io.grpc.stub.StreamObserver;
import org.herts.core.service.HertsServiceClientStreaming;

public class TestClientStreamingServiceInvalid extends HertsServiceClientStreaming<TestClientStreamingServiceInvalid> {
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

    public String test04() {
        return "invalid";
    }
}