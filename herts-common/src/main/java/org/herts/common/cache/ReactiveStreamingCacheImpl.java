package org.herts.common.cache;

import io.grpc.stub.StreamObserver;

import org.herts.common.context.HertsClientInfo;

import java.util.concurrent.ConcurrentHashMap;

public class ReactiveStreamingCacheImpl implements ReactiveStreamingCache {
    private volatile ConcurrentHashMap<String, StreamObserver<Object>> observers = new ConcurrentHashMap<>();
    private volatile ConcurrentHashMap<String, HertsClientInfo> clientInfo = new ConcurrentHashMap<>();
    private static ReactiveStreamingCacheImpl thisClass;

    public static ReactiveStreamingCache getInstance() {
        if (thisClass != null) {
            return thisClass;
        }
        thisClass = new ReactiveStreamingCacheImpl();
        return thisClass;
    }

    @Override
    public void registerObserverToServer(String hertsClientId, StreamObserver<Object> observer) {
        this.observers.put(hertsClientId, observer);
    }

    @Override
    public StreamObserver<Object> getObserver(String hertsClientId) {
        return this.observers.get(hertsClientId);
    }

    @Override
    public boolean removeObserver(String hertsClientId) {
        this.observers.remove(hertsClientId);
        return true;
    }

    @Override
    public void setClientInfo(HertsClientInfo clientInfo) {
        this.clientInfo.put(clientInfo.id, clientInfo);
    }

    @Override
    public HertsClientInfo getClientInfo(String clientId) {
        return this.clientInfo.get(clientId);
    }
}