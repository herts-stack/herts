package com.tomoyane.herts.example;

import com.tomoyane.herts.hertscommon.util.DateUtil;
import com.tomoyane.herts.hertscore.core.StreamingServiceCore;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class StreamingRpcServiceImpl extends StreamingServiceCore implements StreamingRpcService {

    public StreamingRpcServiceImpl() {
    }

    public StreamObserver<HelloResponse> test04(final StreamObserver<HelloResponse> responseObserver) {
        System.out.println("------------ test04 RPC ----------- ");
        return new StreamObserver<>() {
            @Override
            public void onNext(HelloResponse response) {
                var req = new HelloResponse();
                req.setCode(99);
                req.setTimestamp(DateUtil.getCurrentTimeMilliSec());
                responseObserver.onNext(req);
                System.out.println(response.getCode());
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
