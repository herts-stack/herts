package org.herts.e2etest.serverstreaming_rpc;

import org.herts.common.annotation.HertsRpcService;
import org.herts.common.context.HertsType;
import org.herts.common.service.HertsService;
import io.grpc.stub.StreamObserver;
import org.herts.e2etest.common.HelloRequest;
import org.herts.e2etest.common.HelloResponse01;
import org.herts.e2etest.common.HelloResponse02;

@HertsRpcService(value = HertsType.ServerStreaming)
public interface ServerStreamingRpcService extends HertsService {
    void test05(String id, final HelloRequest req, final StreamObserver<HelloResponse01> responseObserver);
    void test01(String id, String id2, final StreamObserver<HelloResponse02> responseObserver);
    void test04(String id, final StreamObserver<Object> responseObserver);
}
