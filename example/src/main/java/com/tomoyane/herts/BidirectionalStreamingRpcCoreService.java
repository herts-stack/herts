package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.service.HertsCoreService;
import io.grpc.stub.StreamObserver;

public interface BidirectionalStreamingRpcCoreService extends HertsCoreService {
    StreamObserver<HelloResponse> test04(final StreamObserver<HelloResponse> responseObserver);
}
