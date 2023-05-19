package org.herts.rpcclient.clientstreaming;

import io.grpc.stub.StreamObserver;
import org.herts.common.annotation.HertsRpcService;
import org.herts.common.context.HertsType;
import org.herts.common.service.HertsService;
import org.herts.rpcclient.TestFoo;
import org.herts.rpcclient.TestHoo;

import java.util.Map;

@HertsRpcService(value = HertsType.ClientStreaming)
public interface TestClientStreamingRpcService extends HertsService {
    StreamObserver<String> test01(final StreamObserver<String> responseObserver);
    StreamObserver<TestHoo> test02(final StreamObserver<TestFoo> responseObserver);
    StreamObserver<Map<String, String>> test03(final StreamObserver<String> responseObserver);
}
