package org.hertsstack.example.serversteaming;

import io.grpc.stub.StreamObserver;
import org.hertsstack.core.annotation.HertsRpcService;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.service.HertsService;

@HertsRpcService(value = HertsType.ServerStreaming)
public interface ServerStreamingService extends HertsService {
    void helloWorld(final StreamObserver<String> responseObserver);
}
