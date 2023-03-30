package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.service.HertsCoreService;
import io.grpc.stub.StreamObserver;

public interface ServerStreamingRpcCoreService extends HertsCoreService {
    void test05(String id, final HelloRequest req, final StreamObserver<HelloResponse> responseObserver);
}
