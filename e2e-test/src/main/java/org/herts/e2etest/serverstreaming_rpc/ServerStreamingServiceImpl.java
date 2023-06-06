package org.herts.e2etest.serverstreaming_rpc;

import org.herts.common.logger.HertsLogger;
import org.herts.common.service.HertsServerStreamingService;
import io.grpc.stub.StreamObserver;
import org.herts.e2etest.common.HelloRequest;
import org.herts.e2etest.common.HelloResponse01;
import org.herts.e2etest.common.HelloResponse02;

import java.util.logging.Logger;

public class ServerStreamingServiceImpl extends HertsServerStreamingService<ServerStreamingRpcService> implements ServerStreamingRpcService {
    private static final Logger logger = HertsLogger.getLogger(ServerStreamingServiceImpl.class.getSimpleName());

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
