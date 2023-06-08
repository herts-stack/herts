package org.herts.example.clientsteaming;

import io.grpc.stub.StreamObserver;
import org.herts.core.annotation.HertsRpcService;
import org.herts.core.context.HertsType;
import org.herts.core.service.HertsService;

@HertsRpcService(value = HertsType.ClientStreaming)
public interface ClientStreamingService extends HertsService {
    StreamObserver<String> helloWorld(StreamObserver<String> responseObserver);
}
