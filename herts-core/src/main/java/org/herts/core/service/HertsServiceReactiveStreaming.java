package org.herts.core.service;

import io.grpc.stub.StreamObserver;
import org.herts.core.context.HertsType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Herts reactive streaming service
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsServiceReactiveStreaming<T, K> extends ReactiveStreamingBase<T, K> implements ReactiveStreaming {

    public HertsServiceReactiveStreaming() {
        super(HertsType.Reactive);
        initialize();
    }

    /**
     * Post construct processing
     */
    private void initialize() {
        Type superClass = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) superClass;
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        this.getBroadCaster().setService((Class<?>) typeArguments[0]);
        this.getBroadCaster().setReceiver((Class<?>) typeArguments[1]);
    }

    /**
     * Get client id for this instance
     *
     * @return ClientId
     */
    public String getClientId() {
        return this.getBroadCaster().getClientId();
    }

    /**
     * Broadcast receiver.
     * Same as generics K type
     *
     * @param clientId ClientId
     * @return Receiver
     */
    public K broadcast(String clientId) {
        return this.getBroadCaster().broadcast(clientId);
    }

    @Override
    public void registerReceiver(StreamObserver<Object> objectStreamObservers) {
        this.getBroadCaster().registerReceiver(objectStreamObservers);
    }
}
