package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.service.HertsCoreService;
import io.grpc.stub.StreamObserver;

public interface ClientStreamingRpcCoreService extends HertsCoreService {
    StreamObserver<HelloRequest> test10(final StreamObserver<HelloResponse> responseObserver);
}
