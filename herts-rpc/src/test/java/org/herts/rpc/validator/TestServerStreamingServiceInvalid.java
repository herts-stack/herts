package org.herts.rpc.validator;

import io.grpc.stub.StreamObserver;
import org.herts.common.service.HertsServerStreamingService;

public class TestServerStreamingServiceInvalid extends HertsServerStreamingService<TestServerStreamingServiceInvalid> {
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
