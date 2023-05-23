package org.herts.common.service;

import io.grpc.stub.StreamObserver;
import org.herts.common.cache.DuplexStreamingCache;
import org.herts.common.cache.DuplexStreamingCacheImpl;
import org.herts.common.context.HertsClientInfo;
import org.herts.common.context.HertsSystemContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collections;

public class HertsBroadCasterImpl implements HertsBroadCaster {
    private final DuplexStreamingCache duplexStreamingCache = DuplexStreamingCacheImpl.getInstance();
    private Class<?> service;
    private Class<?> receiver;
    private HertsReceiver proxyReceiver;
    private String clientId;

    public HertsBroadCasterImpl() {
    }

    @Override
    public <K> K broadcast(String clientId) {
        if (this.duplexStreamingCache == null) {
            return null;
        }
        if (this.proxyReceiver == null) {
            createReceiver(clientId);
        }
        return (K) this.proxyReceiver;
    }

    @Override
    public void registerReceiver(HertsClientInfo clientInfo, StreamObserver<Object> objectStreamObservers) {
        this.duplexStreamingCache.setClientInfo(clientInfo);
        this.duplexStreamingCache.registerObserverToServer(clientInfo.id, objectStreamObservers);
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
        StreamObserver<Object> observer = this.duplexStreamingCache.getObserver(clientId);
        if (observer == null) {
            return;
        }
        InvocationHandler handler = new HertsDuplexStreamingInvoker(observer);
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
