package org.herts.common.service;

import io.grpc.stub.StreamObserver;
import org.herts.common.context.HertsClientInfo;
import org.herts.common.context.HertsType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Herts reactive streaming service
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsReactiveStreamingService<T, K> extends HertsReactiveStreamingServiceBase<T, K> implements HertsReactiveStreamingInternal {

    public HertsReactiveStreamingService() {
        super(HertsType.Reactive);
        Type superClass = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) superClass;
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        this.getBroadCaster().setService( (Class<?>) typeArguments[0]);
        this.getBroadCaster().setReceiver((Class<?>) typeArguments[1]);
    }

    public String getClientId() {
        return this.getBroadCaster().getClientId();
    }

    public K broadcast(String clientId) {
        return this.getBroadCaster().broadcast(clientId);
    }

    @Override
    public void registerReceiver(HertsClientInfo clientInfo, StreamObserver<Object> objectStreamObservers) {
        this.getBroadCaster().registerReceiver(clientInfo, objectStreamObservers);
    }
}