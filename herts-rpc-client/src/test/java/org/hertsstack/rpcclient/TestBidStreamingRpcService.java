package org.hertsstack.rpcclient;

import io.grpc.stub.StreamObserver;
import org.hertsstack.core.annotation.HertsRpcService;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.service.HertsService;

import java.util.Map;

@HertsRpcService(value = HertsType.BidirectionalStreaming)
public interface TestBidStreamingRpcService extends HertsService {
    StreamObserver<String> test01(final StreamObserver<String> responseObserver);
    StreamObserver<TestFoo> test02(final StreamObserver<TestHoo> responseObserver);
    StreamObserver<Map<String, String>> test03(final StreamObserver<Map<String, String>> responseObserver);
    StreamObserver<byte[]> error01(final StreamObserver<byte[]> responseObserver);
    StreamObserver<byte[]> error02(final StreamObserver<byte[]> responseObserver);
}
