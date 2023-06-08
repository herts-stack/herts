package org.herts.example.serversteaming;

import io.grpc.stub.StreamObserver;
import org.herts.core.annotation.HertsRpcService;
import org.herts.core.context.HertsType;
import org.herts.core.service.HertsService;

@HertsRpcService(value = HertsType.ServerStreaming)
public interface ServerStreamingService extends HertsService {
    void helloWorld(final StreamObserver<String> responseObserver);
}
