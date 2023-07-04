package org.hertsstack.e2etest.clientstreaming_rpc;

import org.hertsstack.core.annotation.HertsRpcService;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.service.HertsService;
import io.grpc.stub.StreamObserver;
import org.hertsstack.e2etest.common.HelloRequest;
import org.hertsstack.e2etest.common.HelloResponse01;

@HertsRpcService(value = HertsType.ClientStreaming)
public interface ClientStreamingRpcService extends HertsService {
    StreamObserver<HelloRequest> test10(final StreamObserver<HelloResponse01> responseObserver);
}
