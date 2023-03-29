package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.service.HertsService;
import io.grpc.stub.StreamObserver;

public interface ServerStreamingRpcService extends HertsService {
    void test05(String id, final HelloRequest req, final StreamObserver<HelloResponse> responseObserver);
}
