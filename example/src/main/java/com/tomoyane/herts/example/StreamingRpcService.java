package com.tomoyane.herts.example;

import com.tomoyane.herts.hertscore.service.HertsService;
import io.grpc.stub.StreamObserver;

import java.util.Map;

public interface StreamingRpcService extends HertsService {
    StreamObserver<HelloResponse> test04(final StreamObserver<HelloResponse> responseObserver);
}
