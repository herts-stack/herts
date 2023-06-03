package org.herts.example.serversteaming;

import io.grpc.stub.StreamObserver;
import org.herts.common.annotation.HertsRpcService;
import org.herts.common.context.HertsType;
import org.herts.common.service.HertsService;

@HertsRpcService(value = HertsType.ServerStreaming)
public interface ServerStreamingService extends HertsService {
    void helloWorld(final StreamObserver<String> responseObserver);
}
