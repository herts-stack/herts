package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.util.DateTimeUtil;
import com.tomoyane.herts.hertscore.BidirectionalStreamingServiceCore;
import io.grpc.stub.StreamObserver;

public class BidirectionalStreamingRpcServiceImpl extends BidirectionalStreamingServiceCore implements BidirectionalStreamingRpcService {

    public BidirectionalStreamingRpcServiceImpl() {
    }

    public StreamObserver<HelloResponse> test04(final StreamObserver<HelloResponse> responseObserver) {
        System.out.println("------------ test04 RPC ----------- ");
        return new StreamObserver<>() {
            @Override
            public void onNext(HelloResponse response) {
                var req = new HelloResponse();
                req.setCode(99);
                req.setTimestamp(DateTimeUtil.getCurrentTimeMilliSec());
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
