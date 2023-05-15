package org.herts.rpcclient.serverstreaming;

import io.grpc.stub.StreamObserver;
import org.herts.common.annotation.HertsRpc;
import org.herts.common.context.HertsType;
import org.herts.common.service.HertsService;
import org.herts.rpcclient.TestHoo;

@HertsRpc(value = HertsType.ServerStreaming)
public interface TestServerStreamingRpcService extends HertsService {
    void test01(String id, String id2, final StreamObserver<TestHoo> responseObserver);
    void test02(int id, final StreamObserver<TestHoo> responseObserver);
    void error01(int id, final StreamObserver<byte[]> responseObserver);
}
