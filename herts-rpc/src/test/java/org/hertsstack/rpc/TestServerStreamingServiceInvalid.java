package org.hertsstack.rpc;

import io.grpc.stub.StreamObserver;
import org.hertsstack.core.service.HertsServiceServerStreaming;

public class TestServerStreamingServiceInvalid extends HertsServiceServerStreaming<TestServerStreamingServiceInvalid> {
    public void test01(final StreamObserver<String> responseObserver) {
        responseObserver.onNext("hello");
        responseObserver.onCompleted();
    }

    public String test02(final StreamObserver<String> responseObserver) {
        return "test02";
    }

    public void test03(final StreamObserver<String> responseObserver) {
        responseObserver.onNext("hello");
        responseObserver.onCompleted();
    }

}
