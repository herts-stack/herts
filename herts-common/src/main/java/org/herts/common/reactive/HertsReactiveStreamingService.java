package org.herts.common.reactive;

import io.grpc.stub.StreamObserver;
import org.herts.common.modelx.HertsClientInfo;
import org.herts.common.context.HertsType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Herts reactive streaming service
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsReactiveStreamingService<T, K> extends HertsReactiveStreamingServiceBase<T, K> implements HertsReactiveStreamingInternal {

    public HertsReactiveStreamingService() {
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
