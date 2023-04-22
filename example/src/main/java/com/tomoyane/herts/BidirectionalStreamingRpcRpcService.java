package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.service.HertsRpcService;
import io.grpc.stub.StreamObserver;

public interface BidirectionalStreamingRpcRpcService extends HertsRpcService {
    StreamObserver<HelloResponse> test04(final StreamObserver<HelloResponse> responseObserver);
}
