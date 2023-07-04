package org.hertsstack.example.clientsteaming;

import io.grpc.stub.StreamObserver;
import org.hertsstack.core.annotation.HertsRpcService;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.service.HertsService;

@HertsRpcService(value = HertsType.ClientStreaming)
public interface ClientStreamingService extends HertsService {
    StreamObserver<String> helloWorld(StreamObserver<String> responseObserver);
}
