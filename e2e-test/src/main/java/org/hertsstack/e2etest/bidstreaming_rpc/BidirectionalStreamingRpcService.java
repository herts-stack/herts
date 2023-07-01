package org.hertsstack.e2etest.bidstreaming_rpc;

import org.hertsstack.core.annotation.HertsRpcService;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.service.HertsService;
import io.grpc.stub.StreamObserver;
import org.hertsstack.e2etest.common.HelloResponse01;

@HertsRpcService(value = HertsType.BidirectionalStreaming)
public interface BidirectionalStreamingRpcService extends HertsService {
    StreamObserver<HelloResponse01> test04(final StreamObserver<HelloResponse01> responseObserver);
}
