package org.herts.example;

import org.herts.common.service.HertsRpcService;
import io.grpc.stub.StreamObserver;

public interface ServerStreamingRpcRpcService extends HertsRpcService {
    void test05(String id, final HelloRequest req, final StreamObserver<HelloResponse> responseObserver);
}
