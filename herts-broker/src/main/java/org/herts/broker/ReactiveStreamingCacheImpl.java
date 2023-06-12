package org.herts.broker;

import io.grpc.stub.StreamObserver;

import java.util.concurrent.ConcurrentHashMap;

/**
 * HertsReactive cache local implementation.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class ReactiveStreamingCacheImpl<T> implements ReactiveStreamingCache<T> {
    private final ConcurrentHashMap<String, T> receivers;
    private final ConcurrentHashMap<String, StreamObserver<Object>> observers;
    private final ConcurrentHashMap<String, String> clientId;
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
    @SuppressWarnings("unchecked")
    public static <T> ReactiveStreamingCache<T> getInstance() {
        if (thisClass != null) {
            return thisClass;
        }
        thisClass = new ReactiveStreamingCacheImpl<T>();
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
    public void setHertsReceiver(String hertsClientId, T hertsReceiver) {
        this.receivers.put(hertsClientId, hertsReceiver);
    }

    @Override
    public T getHertsReceiver(String hertsClientId) {
        return this.receivers.get(hertsClientId);
    }
}