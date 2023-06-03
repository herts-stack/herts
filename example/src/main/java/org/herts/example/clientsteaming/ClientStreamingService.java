package org.herts.example.clientsteaming;

import io.grpc.stub.StreamObserver;
import org.herts.common.annotation.HertsRpcService;
import org.herts.common.context.HertsType;
import org.herts.common.service.HertsService;

@HertsRpcService(value = HertsType.ClientStreaming)
public interface ClientStreamingService extends HertsService {
    StreamObserver<String> helloWorld(StreamObserver<String> responseObserver);
}
