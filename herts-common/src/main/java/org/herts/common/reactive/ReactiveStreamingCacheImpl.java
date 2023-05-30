package org.herts.common.reactive;

import io.grpc.stub.StreamObserver;

import org.herts.common.modelx.HertsClientInfo;
import org.herts.common.modelx.HertsReceiverInfo;

import java.util.concurrent.ConcurrentHashMap;

/**
 * HertsReactive cache local implementation.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class ReactiveStreamingCacheImpl implements ReactiveStreamingCache {
    private volatile ConcurrentHashMap<String, HertsReceiverInfo> receivers;
    private volatile ConcurrentHashMap<String, StreamObserver<Object>> observers;
    private volatile ConcurrentHashMap<String, String> clientId;
    private static ReactiveStreamingCacheImpl thisClass;

    private ReactiveStreamingCacheImpl() {
        this.receivers = new ConcurrentHashMap<>();
        this.observers = new ConcurrentHashMap<>();
        this.clientId = new ConcurrentHashMap<>();
    }

    /**
     * Singleton instance.
     *
     * @return ReactiveStreamingCache
     */
    public static ReactiveStreamingCache getInstance() {
        if (thisClass != null) {
            return thisClass;
        }
        thisClass = new ReactiveStreamingCacheImpl();
        return thisClass;
    }

    @Override
    public void setObserver(String hertsClientId, StreamObserver<Object> observer) {
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
    public void setClientId(String clientId) {
        this.clientId.put(clientId, clientId);
    }

    @Override
    public String getClientId(String clientId) {
        return this.clientId.get(clientId);
    }

    @Override
    public String[] getClientIds() {
        return this.clientId.keySet().toArray(new String[0]);
    }

    @Override
    public void setHertsReceiver(String hertsClientId, HertsReceiver hertsReceiver, HertsReactiveStreamingInvoker invoker) {
        var receiver = new HertsReceiverInfo(hertsReceiver, invoker);
        this.receivers.put(hertsClientId, receiver);
    }

    @Override
    public HertsReceiverInfo getHertsReceiver(String hertsClientId) {
        return this.receivers.get(hertsClientId);
    }
}