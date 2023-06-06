package org.herts.common.reactive;

import io.grpc.stub.StreamObserver;
import org.herts.common.context.HertsSystemContext;
import org.herts.common.loadbalancing.HertsBroker;
import org.herts.common.modelx.HertsReceiverInfo;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * HertsBroadCaster implementation
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsBroadCasterImpl implements HertsBroadCaster {
    private static final Logger logger = Logger.getLogger(HertsBroadCasterImpl.class.getName());

    private final ReactiveStreamingCache reactiveStreamingCache = ReactiveStreamingCacheImpl.getInstance();
    private HertsBroker broker;
    private Class<?> service;
    private Class<?> receiver;

    public HertsBroadCasterImpl() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K> K broadcast(String clientId) {
        return (K) createReceiver(clientId);
    }

    @Override
    public void registerReceiver(StreamObserver<Object> objectStreamObservers) {
        String clientId = HertsSystemContext.Header.HERTS_CONNECTION_ID_CTX.get();
        this.reactiveStreamingCache.setClientId(clientId);
        this.reactiveStreamingCache.setObserver(clientId, objectStreamObservers);
        objectStreamObservers.onNext(Collections.singletonList(HertsSystemContext.Rpc.REGISTERED_METHOD_NAME));
        createReceiver(clientId);
    }

    @Override
    public String getClientId() {
        return HertsSystemContext.Header.HERTS_CONNECTION_ID_CTX.get();
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

    private void printClientIds() {
        logger.info("Debug client id stat");
        for (String id : this.reactiveStreamingCache.getClientIds()) {
            logger.info(id);
        }
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

        HertsReactiveStreamingInvoker handler = new HertsReactiveStreamingInvoker(this.broker, clientId);
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
