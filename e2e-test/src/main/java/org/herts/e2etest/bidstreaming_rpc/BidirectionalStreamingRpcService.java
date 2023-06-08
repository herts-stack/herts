package org.herts.e2etest.bidstreaming_rpc;

import org.herts.core.annotation.HertsRpcService;
import org.herts.core.context.HertsType;
import org.herts.core.service.HertsService;
import io.grpc.stub.StreamObserver;
import org.herts.e2etest.common.HelloResponse01;

@HertsRpcService(value = HertsType.BidirectionalStreaming)
public interface BidirectionalStreamingRpcService extends HertsService {
    StreamObserver<HelloResponse01> test04(final StreamObserver<HelloResponse01> responseObserver);
}
