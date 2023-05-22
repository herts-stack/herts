package org.herts.example.serverstreaming_rpc;

import com.google.protobuf.Descriptors;
import org.herts.common.annotation.HertsRpcService;
import org.herts.common.context.HertsType;
import org.herts.common.service.HertsService;
import io.grpc.stub.StreamObserver;
import org.herts.example.common.HelloRequest;
import org.herts.example.common.HelloResponse01;
import org.herts.example.common.HelloResponse02;

@HertsRpcService(value = HertsType.ServerStreaming)
public interface ServerStreamingRpcService extends HertsService {
    void test05(String id, final HelloRequest req, final StreamObserver<HelloResponse01> responseObserver);
    void test01(String id, String id2, final StreamObserver<HelloResponse02> responseObserver);
    void test04(String id, final StreamObserver<Object> responseObserver);
}
