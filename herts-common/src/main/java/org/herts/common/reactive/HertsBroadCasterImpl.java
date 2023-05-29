package org.herts.common.reactive;

import io.grpc.stub.StreamObserver;
import org.herts.common.modelx.HertsClientInfo;
import org.herts.common.context.HertsSystemContext;
import org.herts.common.loadbalancing.HertsBroker;
import org.herts.common.modelx.HertsReceiverInfo;

import java.lang.reflect.Proxy;
import java.util.Collections;

/**
 * HertsBroadCaster implementation
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsBroadCasterImpl implements HertsBroadCaster {
    private final ReactiveStreamingCache reactiveStreamingCache = ReactiveStreamingCacheImpl.getInstance();
    private HertsBroker broker;
    private Class<?> service;
    private Class<?> receiver;
    private String clientId;

    public HertsBroadCasterImpl() {
    }

    @Override
    public <K> K broadcast(String clientId) {
        return (K) createReceiver(clientId);
    }

    @Override
    public void registerReceiver(HertsClientInfo clientInfo, StreamObserver<Object> objectStreamObservers) {
        this.reactiveStreamingCache.setClientInfo(clientInfo);
        this.reactiveStreamingCache.setObserver(clientInfo.id, objectStreamObservers);
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
    public void setBroker(HertsBroker broker) {
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
        HertsReceiverInfo hertsReceiverInfo = this.reactiveStreamingCache.getHertsReceiver(clientId);
        if (hertsReceiverInfo != null) {
            return hertsReceiverInfo.getReceiver();
        }
        StreamObserver<Object> observer = this.reactiveStreamingCache.getObserver(clientId);
        if (observer == null) {
            return null;
        }

        var handler = new HertsReactiveStreamingInvoker(this.broker, clientId);
        try {
            HertsReceiver hertsReceiver = (HertsReceiver) Proxy.newProxyInstance(
                    this.receiver.getClassLoader(),
                    new Class<?>[]{this.receiver},
                    handler);

            this.reactiveStreamingCache.setHertsReceiver(clientId, hertsReceiver, handler);
            return hertsReceiver;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
