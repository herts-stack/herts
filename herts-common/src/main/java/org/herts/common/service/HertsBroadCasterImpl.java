package org.herts.common.service;

import io.grpc.stub.StreamObserver;
import org.herts.common.cache.ReactiveStreamingCache;
import org.herts.common.cache.ReactiveStreamingLocalCacheImpl;
import org.herts.common.context.HertsClientInfo;
import org.herts.common.context.HertsSystemContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collections;

/**
 * HertsBroadCaster implementation
 * Wrapped java.util.logging.Logger
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsBroadCasterImpl implements HertsBroadCaster {
    private final ReactiveStreamingCache reactiveStreamingCache = ReactiveStreamingLocalCacheImpl.getInstance();
    private Class<?> service;
    private Class<?> receiver;
    private HertsReceiver proxyReceiver;
    private String clientId;

    public HertsBroadCasterImpl() {
    }

    @Override
    public <K> K broadcast(String clientId) {
        if (this.reactiveStreamingCache == null) {
            return null;
        }
        if (this.proxyReceiver == null) {
            createReceiver(clientId);
        }
        return (K) this.proxyReceiver;
    }

    @Override
    public void registerReceiver(HertsClientInfo clientInfo, StreamObserver<Object> objectStreamObservers) {
        this.reactiveStreamingCache.setClientInfo(clientInfo);
        this.reactiveStreamingCache.registerObserverToServer(clientInfo.id, objectStreamObservers);
        objectStreamObservers.onNext(Collections.singletonList(HertsSystemContext.Rpc.REGISTERED_METHOD_NAME));
        this.clientId = clientInfo.id;
        createReceiver(clientInfo.id);
    }

    @Override
    public String getClientId() {
        return this.clientId;
    }

    @Override
    public void setService(Class<?> service) {
        this.service = service;
    }

    @Override
    public void setReceiver(Class<?> receiver) {
        this.receiver = receiver;
    }

    @Override
    public Class<?> getService() {
        return this.service;
    }

    @Override
    public Class<?> getReceiver() {
        return this.receiver;
    }

    private void createReceiver(String clientId) {
        StreamObserver<Object> observer = this.reactiveStreamingCache.getObserver(clientId);
        if (observer == null) {
            return;
        }
        InvocationHandler handler = new HertsReactiveStreamingInvoker(observer);
        try {
            this.proxyReceiver = (HertsReceiver) Proxy.newProxyInstance(
                    this.receiver.getClassLoader(),
                    new Class<?>[]{ this.receiver },
                    handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
