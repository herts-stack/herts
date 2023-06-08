package org.herts.example.bidsteaming;

import io.grpc.stub.StreamObserver;
import org.herts.core.annotation.HertsRpcService;
import org.herts.core.context.HertsType;
import org.herts.core.service.HertsService;

@HertsRpcService(value = HertsType.BidirectionalStreaming)
public interface BidStreamingService extends HertsService {
    StreamObserver<String> helloWorld(StreamObserver<String> responseObserver);
}
