package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.service.HertsService;
import io.grpc.stub.StreamObserver;

public interface ClientStreamingRpcService extends HertsService {
    StreamObserver<HelloRequest> test10(final StreamObserver<HelloResponse> responseObserver);
}