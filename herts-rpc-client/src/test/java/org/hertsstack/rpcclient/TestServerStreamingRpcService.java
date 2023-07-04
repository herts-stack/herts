package org.hertsstack.rpcclient;

import io.grpc.stub.StreamObserver;
import org.hertsstack.core.annotation.HertsRpcService;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.service.HertsService;

@HertsRpcService(value = HertsType.ServerStreaming)
public interface TestServerStreamingRpcService extends HertsService {
    void test01(String id, String id2, final StreamObserver<TestHoo> responseObserver);
    void test02(int id, final StreamObserver<TestHoo> responseObserver);
    void error01(int id, final StreamObserver<byte[]> responseObserver);
}
