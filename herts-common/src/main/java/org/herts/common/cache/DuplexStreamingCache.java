package org.herts.common.cache;

import io.grpc.stub.StreamObserver;
import org.herts.common.context.HertsClientInfo;

import java.util.List;

public interface DuplexStreamingCache {
    void registerObserverToServer(String hertsClientId, StreamObserver<Object> observer);
    StreamObserver<Object> getObserver(String hertsClientId);
    boolean removeObserver(String hertsClientId);

    void setClientInfo(HertsClientInfo clientInfo);
    HertsClientInfo getClientInfo(String clientId);
}