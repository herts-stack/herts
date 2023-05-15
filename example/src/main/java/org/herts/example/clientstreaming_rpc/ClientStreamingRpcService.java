package org.herts.example.clientstreaming_rpc;

import org.herts.common.annotation.HertsRpc;
import org.herts.common.context.HertsType;
import org.herts.common.service.HertsService;
import io.grpc.stub.StreamObserver;
import org.herts.example.common.HelloRequest;
import org.herts.example.common.HelloResponse01;

@HertsRpc(value = HertsType.ClientStreaming)
public interface ClientStreamingRpcService extends HertsService {
    StreamObserver<HelloRequest> test10(final StreamObserver<HelloResponse01> responseObserver);
}
