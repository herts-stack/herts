package org.hertsstack.e2etest.serverstreaming_rpc;

import org.hertsstack.core.logger.Logging;
import org.hertsstack.core.service.HertsServiceServerStreaming;
import io.grpc.stub.StreamObserver;
import org.hertsstack.e2etest.common.HelloRequest;
import org.hertsstack.e2etest.common.HelloResponse01;
import org.hertsstack.e2etest.common.HelloResponse02;

public class ServerStreamingServiceImpl extends HertsServiceServerStreaming<ServerStreamingRpcService> implements ServerStreamingRpcService {
    private static final java.util.logging.Logger logger = Logging.getLogger(ServerStreamingServiceImpl.class.getSimpleName());

    public ServerStreamingServiceImpl() {
    }

    @Override
    public void test05(String id, HelloRequest req, StreamObserver<HelloResponse01> responseObserver) {
        logger.info("------------ ServerStreaming test05 RPC");
        HelloResponse01 res = new HelloResponse01();
        res.setCode(9999);
        responseObserver.onNext(res);
        responseObserver.onCompleted();
    }

    @Override
    public void test01(String id, String id2, StreamObserver<HelloResponse02> responseObserver) {
        for (int i = 1; i <= 10; i++) {
            HelloResponse02 foo = new HelloResponse02();
            foo.setA01("_" + i);
            foo.setB01(i);
            responseObserver.onNext(foo);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void test04(String id, StreamObserver<Object> responseObserver) {
        for (int i = 1; i <= 10; i++) {
            responseObserver.onNext("hey");
        }
        responseObserver.onCompleted();
    }
}
