package org.hertsstack.rpcclient;

import io.grpc.stub.StreamObserver;
import org.hertsstack.core.service.HertsServiceServerStreaming;

import java.util.Collections;

public class TestServerStreamingRpcServiceImpl extends HertsServiceServerStreaming<TestServerStreamingRpcService> implements TestServerStreamingRpcService {

    @Override
    public void test01(String id, String id2, final StreamObserver<TestHoo> responseObserver) {
        for (int i = 1; i <= 10; i++) {
            TestHoo foo = new TestHoo();
            foo.setA01("_" + i);
            foo.setB01(i);
            foo.setC01(0.1);
            foo.setD01(Collections.singletonMap("key", "val"));
            foo.setE01(Collections.singletonList("hi"));
            foo.setF01(Collections.emptySet());
            responseObserver.onNext(foo);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void test02(int id, StreamObserver<TestHoo> responseObserver) {
        TestHoo foo = new TestHoo();
        foo.setB01(id);
        responseObserver.onNext(foo);
        responseObserver.onCompleted();
    }

    @Override
    public void error01(int id, StreamObserver<byte[]> responseObserver) {
        throw new RuntimeException();
    }
}
