package org.herts.e2etest.clientstreaming_rpc;

import org.herts.common.annotation.HertsRpcService;
import org.herts.common.context.HertsType;
import org.herts.common.service.HertsService;
import io.grpc.stub.StreamObserver;
import org.herts.e2etest.common.HelloRequest;
import org.herts.e2etest.common.HelloResponse01;

@HertsRpcService(value = HertsType.ClientStreaming)
public interface ClientStreamingRpcService extends HertsService {
    StreamObserver<HelloRequest> test10(final StreamObserver<HelloResponse01> responseObserver);
}
