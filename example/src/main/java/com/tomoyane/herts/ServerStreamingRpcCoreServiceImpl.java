package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import com.tomoyane.herts.hertscommon.service.ServerStreamingCoreServiceCore;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class ServerStreamingRpcCoreServiceImpl extends ServerStreamingCoreServiceCore implements ServerStreamingRpcCoreService {
    private static final Logger logger = HertsLogger.getLogger(ServerStreamingRpcCoreServiceImpl.class.getSimpleName());

    public ServerStreamingRpcCoreServiceImpl() {
    }

    @Override
    public void test05(String id, HelloRequest req, StreamObserver<HelloResponse> responseObserver) {
        logger.info("------------ ServerStreaming test05 RPC");
        var res = new HelloResponse();
        res.setCode(9999);
        responseObserver.onNext(res);
        responseObserver.onCompleted();
    }
}
