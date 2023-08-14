package org.hertsstack.core.service;

import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import org.hertsstack.broker.ReactiveBroker;
import org.hertsstack.broker.ReactiveStreamingCache;
import org.hertsstack.broker.ReactiveStreamingCacheImpl;
import org.hertsstack.core.context.SharedServiceContext;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * HertsBroadCaster implementation
 *
 * @author Herts Contributer
 */
class HertsBroadCasterImpl implements HertsBroadCaster {
    private static final Logger logger = Logger.getLogger(HertsBroadCasterImpl.class.getName());

    private final ReactiveStreamingCache<HertsReceiver> reactiveStreamingCache = ReactiveStreamingCacheImpl.getInstance();
    private ReactiveBroker broker;
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
    public <K> K broadcast(String[] clientIds) {
        return null;
    }

    @Override
    public <K> K broadcastToGroup(String groupName) {
        return null;
    }

    @Override
    public void registerReceiver(StreamObserver<Object> objectStreamObservers) {
        ServerCallStreamObserver<Object> serverCallStreamObserver = (ServerCallStreamObserver<Object>) objectStreamObservers;
        serverCallStreamObserver.setOnCancelHandler(() -> {
            logger.info("Cancelled internal receiver of Herts");
        });
        String clientId = SharedServiceContext.Header.HERTS_CONNECTION_ID_CTX.get();
        this.reactiveStreamingCache.setClientId(clientId);
        this.reactiveStreamingCache.setObserver(clientId, objectStreamObservers);
        objectStreamObservers.onNext(Collections.singletonList(SharedServiceContext.Rpc.REGISTERED_METHOD_NAME));
        createReceiver(clientId);
    }

    @Override
    public String getClientId() {
        return SharedServiceContext.Header.HERTS_CONNECTION_ID_CTX.get();
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
    public void setBroker(ReactiveBroker broker) {
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
        HertsReceiver hertsReceiver = this.reactiveStreamingCache.getHertsReceiver(clientId);
        if (hertsReceiver != null) {
            return hertsReceiver;
        }
        HertsReactiveStreamingInvoker handler = new HertsReactiveStreamingInvoker(this.broker, clientId);
        try {
            hertsReceiver = (HertsReceiver) Proxy.newProxyInstance(
                    this.receiver.getClassLoader(),
                    new Class<?>[]{this.receiver},
                    handler);

            this.reactiveStreamingCache.setHertsReceiver(clientId, hertsReceiver);
            return hertsReceiver;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
