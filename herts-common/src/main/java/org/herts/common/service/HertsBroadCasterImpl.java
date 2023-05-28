package org.herts.common.service;

import io.grpc.stub.StreamObserver;
import org.herts.common.cache.ReactiveStreamingCache;
import org.herts.common.cache.ReactiveStreamingLocalCacheImpl;
import org.herts.common.cache.ReactiveStreamingReceiverCache;
import org.herts.common.cache.ReactiveStreamingReceiverCacheImpl;
import org.herts.common.context.HertsClientInfo;
import org.herts.common.context.HertsSystemContext;
import org.herts.common.loadbalancing.HertsMessageBroker;
import org.herts.common.loadbalancing.local.ConcurrentLocalBroker;

import java.lang.reflect.Proxy;
import java.util.Collections;

/**
 * HertsBroadCaster implementation
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsBroadCasterImpl implements HertsBroadCaster {
    private final ReactiveStreamingCache reactiveStreamingCache = ReactiveStreamingLocalCacheImpl.getInstance();
    private final ReactiveStreamingReceiverCache reactiveStreamingReceiverCache;
    private HertsMessageBroker broker;
    private Class<?> service;
    private Class<?> receiver;
    private String clientId;

    public HertsBroadCasterImpl() {
        this.reactiveStreamingReceiverCache = new ReactiveStreamingReceiverCacheImpl();
    }

    @Override
    public <K> K broadcast(String clientId) {
        HertsReceiver hertsReceiver;
        var proxyReceiver = this.reactiveStreamingReceiverCache.getHertsReceiver(clientId);
        if (proxyReceiver == null) {
            hertsReceiver = createReceiver(clientId);
        } else {
            proxyReceiver.getInvoker().setTarget(clientId);
            hertsReceiver = proxyReceiver.getReceiver();
        }
        return (K) hertsReceiver;
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
    public void setBroker(HertsMessageBroker broker) {
        this.broker = broker;
    }

    @Override
    public Class<?> getService() {
        return this.service;
    }

    @Override
    public Class<?> getReceiver() {
        return this.receiver;
    }

    private HertsReceiver createReceiver(String clientId) {
        StreamObserver<Object> observer = this.reactiveStreamingCache.getObserver(clientId);
        if (observer == null) {
            return null;
        }

        HertsReactiveStreamingInvoker handler = new HertsReactiveStreamingInvoker(this.broker);
        handler.setTarget(clientId);
        HertsReceiver hertsReceiver;
        try {
            hertsReceiver = (HertsReceiver) Proxy.newProxyInstance(
                    this.receiver.getClassLoader(),
                    new Class<?>[]{this.receiver},
                    handler);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        this.reactiveStreamingReceiverCache.setHertsReceiver(clientId, hertsReceiver, handler);
        return hertsReceiver;
    }
}
