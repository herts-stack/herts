package org.herts.broker;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

/**
 * HertsReactive cache local implementation.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class ReactiveStreamingCacheImpl<T> implements ReactiveStreamingCache<T> {
    private final Cache<String, T> receivers;
    private final Cache<String, StreamObserver<Object>> observers;
    private final Cache<String, String> clientId;
    private static ReactiveStreamingCacheImpl thisClass;

    private ReactiveStreamingCacheImpl() {
        this.receivers = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
        this.observers = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
        this.clientId = CacheBuilder.newBuilder()
                .expireAfterAccess(30, TimeUnit.DAYS)
                .build();
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
    public void setObserver(String clientId, StreamObserver<Object> observer) {
        this.observers.put(clientId, observer);
    }

    @Override
    public StreamObserver<Object> getObserver(String clientId) {
        return this.observers.getIfPresent(clientId);
    }

    @Override
    public boolean removeObserver(String clientId) {
        this.observers.invalidate(clientId);
        return true;
    }

    @Override
    public void setClientId(String clientId) {
        this.clientId.put(clientId, clientId);
    }

    @Override
    public String getClientId(String clientId) {
        return this.clientId.getIfPresent(clientId);
    }

    @Override
    public String[] getClientIds() {
        return this.clientId.asMap().keySet().toArray(new String[0]);
    }

    @Override
    public void setHertsReceiver(String clientId, T receiver) {
        this.receivers.put(clientId, receiver);
    }

    @Override
    public T getHertsReceiver(String clientId) {
        return this.receivers.getIfPresent(clientId);
    }
}