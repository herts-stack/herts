package com.tomoyane.herts;

import com.tomoyane.herts.hertscore.ClientStreamingServiceCore;
import io.grpc.stub.StreamObserver;

public class ClientStreamingRpcServiceImpl extends ClientStreamingServiceCore implements ClientStreamingRpcService {

    public ClientStreamingRpcServiceImpl() {
    }

    @Override
    public StreamObserver<HelloRequest> test10(StreamObserver<HelloResponse> responseObserver) {
        System.out.println("------------ test10 RPC ----------- ");
        return new StreamObserver<>() {
            @Override
            public void onNext(HelloRequest response) {
                System.out.println("------------ " + response.getNumber());
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
