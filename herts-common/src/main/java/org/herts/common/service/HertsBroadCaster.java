package org.herts.common.service;

import io.grpc.stub.StreamObserver;
import org.herts.common.context.HertsClientInfo;

public interface HertsBroadCaster {
    <K> K broadcast(String clientId);
    void registerReceiver(HertsClientInfo clientInfo, StreamObserver<Object> objectStreamObservers);
    void setService(Class<?> service);
    void setReceiver(Class<?> receiver);

    Class<?> getService();
    Class<?> getReceiver();
    String getClientId();
}
