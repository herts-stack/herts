package org.herts.common.cache;

import io.grpc.stub.StreamObserver;

import org.herts.common.context.HertsClientInfo;

import java.util.concurrent.ConcurrentHashMap;

/**
 * HertsReactive cache local implementation.
 * @author Herts Contributer
 * @version 1.0.0
 */
public class ReactiveStreamingLocalCacheImpl implements ReactiveStreamingCache {
    private volatile ConcurrentHashMap<String, StreamObserver<Object>> observers;
    private volatile ConcurrentHashMap<String, HertsClientInfo> clientInfo;
    private static ReactiveStreamingLocalCacheImpl thisClass;

    private ReactiveStreamingLocalCacheImpl() {
        this.observers = new ConcurrentHashMap<>();
        this.clientInfo = new ConcurrentHashMap<>();
    }

    /**
     * Singleton instance.
     * @return ReactiveStreamingCache
     */
    public static ReactiveStreamingCache getInstance() {
        if (thisClass != null) {
            return thisClass;
        }
        thisClass = new ReactiveStreamingLocalCacheImpl();
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