package org.herts.rpcclient.receiver;

import io.grpc.stub.StreamObserver;

import java.util.List;

public class ReceiveObserver implements StreamObserver<Object> {
    @Override
    public void onNext(Object value) {
        List<Object> receivedData = (List<Object>) value;
        System.out.println("Received! " + receivedData.get(0));
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @Override
    public void onCompleted() {
        System.out.println("Comp!");
    }
}
