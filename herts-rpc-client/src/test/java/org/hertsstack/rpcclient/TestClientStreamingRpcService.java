package org.hertsstack.rpcclient;

import io.grpc.stub.StreamObserver;
import org.hertsstack.core.annotation.HertsRpcService;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.service.HertsService;

import java.util.Map;

@HertsRpcService(value = HertsType.ClientStreaming)
public interface TestClientStreamingRpcService extends HertsService {
    StreamObserver<String> test01(final StreamObserver<String> responseObserver);
    StreamObserver<TestHoo> test02(final StreamObserver<TestFoo> responseObserver);
    StreamObserver<Map<String, String>> test03(final StreamObserver<String> responseObserver);
}
