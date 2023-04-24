package org.herts.example;

import org.herts.common.service.HertsRpcService;
import io.grpc.stub.StreamObserver;

public interface BidirectionalStreamingRpcRpcService extends HertsRpcService {
    StreamObserver<HelloResponse> test04(final StreamObserver<HelloResponse> responseObserver);
}
