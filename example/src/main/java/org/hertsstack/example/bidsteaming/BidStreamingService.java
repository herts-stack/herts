package org.hertsstack.example.bidsteaming;

import io.grpc.stub.StreamObserver;
import org.hertsstack.core.annotation.HertsRpcService;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.service.HertsService;

@HertsRpcService(value = HertsType.BidirectionalStreaming)
public interface BidStreamingService extends HertsService {
    StreamObserver<String> helloWorld(StreamObserver<String> responseObserver);
}
