package org.herts.rpcclient;

import io.grpc.stub.StreamObserver;
import org.herts.core.annotation.HertsRpcService;
import org.herts.core.context.HertsType;
import org.herts.core.service.HertsService;
import org.herts.rpcclient.TestHoo;

@HertsRpcService(value = HertsType.ServerStreaming)
public interface TestServerStreamingRpcService extends HertsService {
    void test01(String id, String id2, final StreamObserver<TestHoo> responseObserver);
    void test02(int id, final StreamObserver<TestHoo> responseObserver);
    void error01(int id, final StreamObserver<byte[]> responseObserver);
}
