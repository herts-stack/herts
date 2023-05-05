package org.herts.example;

import org.herts.common.logger.HertsLogger;
import org.herts.common.service.HertsServerStreamingService;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class ServerStreamingServiceServiceImpl extends HertsServerStreamingService<ServerStreamingRpcService> implements ServerStreamingRpcService {
    private static final Logger logger = HertsLogger.getLogger(ServerStreamingServiceServiceImpl.class.getSimpleName());

    public ServerStreamingServiceServiceImpl() {
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
