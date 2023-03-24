package com.tomoyane.herts.example;

import com.tomoyane.herts.hertscore.service.HertsService;
import io.grpc.stub.StreamObserver;

public interface ClientStreamingRpcService extends HertsService {
    StreamObserver<HelloRequest> test10(final StreamObserver<HelloResponse> responseObserver);
}
