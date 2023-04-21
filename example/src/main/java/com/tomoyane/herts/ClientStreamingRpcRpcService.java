package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.service.HertsRpcService;
import io.grpc.stub.StreamObserver;

public interface ClientStreamingRpcRpcService extends HertsRpcService {
    StreamObserver<HelloRequest> test10(final StreamObserver<HelloResponse> responseObserver);
}
