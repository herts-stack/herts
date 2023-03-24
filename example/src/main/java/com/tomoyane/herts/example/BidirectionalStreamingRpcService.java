package com.tomoyane.herts.example;

import com.tomoyane.herts.hertscore.service.HertsService;
import io.grpc.stub.StreamObserver;

public interface BidirectionalStreamingRpcService extends HertsService {
    StreamObserver<HelloResponse> test04(final StreamObserver<HelloResponse> responseObserver);
}
