package org.herts.common.service;

import io.grpc.stub.StreamObserver;
import org.herts.common.context.HertsClientInfo;

import java.util.List;

public interface HertsDuplexInternalStreaming {
    void registerReceiver(HertsClientInfo clientInfo, StreamObserver<Object> objectStreamObservers);
}
