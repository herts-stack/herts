package org.hertsstack.e2etest.serverstreaming_rpc;

import org.hertsstack.core.annotation.HertsRpcService;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.service.HertsService;
import io.grpc.stub.StreamObserver;
import org.hertsstack.e2etest.common.HelloRequest;
import org.hertsstack.e2etest.common.HelloResponse01;
import org.hertsstack.e2etest.common.HelloResponse02;

@HertsRpcService(value = HertsType.ServerStreaming)
public interface ServerStreamingRpcService extends HertsService {
    void test05(String id, final HelloRequest req, final StreamObserver<HelloResponse01> responseObserver);
    void test01(String id, String id2, final StreamObserver<HelloResponse02> responseObserver);
    void test04(String id, final StreamObserver<Object> responseObserver);
}
