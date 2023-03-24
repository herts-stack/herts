package com.tomoyane.herts;

import com.tomoyane.herts.hertscore.core.ServerStreamingServiceCore;
import io.grpc.stub.StreamObserver;

public class ServerStreamingRpcServiceImpl extends ServerStreamingServiceCore implements ServerStreamingRpcService {

    public ServerStreamingRpcServiceImpl() {
    }

    @Override
    public void test05(String id, HelloRequest req, StreamObserver<HelloResponse> responseObserver) {
        System.out.println("------------ test05 RPC ----------- ");
        System.out.println(id);
        System.out.println(req.getNumber());

        var res = new HelloResponse();
        res.setCode(9999);
        responseObserver.onNext(res);
        responseObserver.onCompleted();
    }
}
