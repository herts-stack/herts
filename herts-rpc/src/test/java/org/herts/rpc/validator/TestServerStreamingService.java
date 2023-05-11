package org.herts.rpc.validator;

import io.grpc.stub.StreamObserver;
import org.herts.common.service.HertsServerStreamingService;

public class TestServerStreamingService extends HertsServerStreamingService<TestServerStreamingService> {
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
