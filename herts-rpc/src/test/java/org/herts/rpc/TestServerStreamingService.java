package org.herts.rpc;

import io.grpc.stub.StreamObserver;
import org.herts.core.service.HertsServiceServerStreaming;

public class TestServerStreamingService extends HertsServiceServerStreaming<TestServerStreamingService> {
    public void test01(final StreamObserver<String> responseObserver) {
        responseObserver.onNext("hello");
        responseObserver.onCompleted();
    }

    public void test02(final StreamObserver<String> responseObserver) {
        responseObserver.onNext("hello");
        responseObserver.onCompleted();
    }

    public void test03(final StreamObserver<String> responseObserver) {
        responseObserver.onNext("hello");
        responseObserver.onCompleted();
    }

}
