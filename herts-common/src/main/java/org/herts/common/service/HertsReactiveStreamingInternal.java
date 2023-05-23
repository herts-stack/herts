package org.herts.common.service;

import io.grpc.stub.StreamObserver;
import org.herts.common.context.HertsClientInfo;

public interface HertsReactiveStreamingInternal {
    void registerReceiver(HertsClientInfo clientInfo, StreamObserver<Object> objectStreamObservers);
}
