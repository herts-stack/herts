package org.herts.common.reactive;

import io.grpc.stub.StreamObserver;
import org.herts.common.modelx.HertsClientInfo;
import org.herts.common.loadbalancing.HertsBroker;

/**
 * HertsBroadCaster for internal reactive streaming.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsBroadCaster {
    /**
     * Get Broadcast receiver interface.
     * The receiver is what you add K in HertsReactiveStreamingService
     *
     * @param clientId ClientId
     * @return Receiver interface
     */
    <K> K broadcast(String clientId);

    /**
     * Register receiver for internal processing.
     *
     * @param objectStreamObservers StreamObserver
     */
    void registerReceiver(StreamObserver<Object> objectStreamObservers);

    /**
     * Set HertsService.
     *
     * @param service HertsService implementation
     */
    void setService(Class<?> service);

    /**
     * Set HertsReceiver
     *
     * @param receiver HertsReceiver implementation
     */
    void setReceiver(Class<?> receiver);

    /**
     * Set herts broker.
     *
     * @param broker HertsMessageBroker
     */
    void setBroker(HertsBroker broker);

    /**
     * Get HertsService
     *
     * @return HertsService
     */
    Class<?> getService();

    /**
     * Get HertsReceiver
     *
     * @return HertsReceiver
     */
    Class<?> getReceiver();

    /**
     * Get Client id
     *
     * @return ClientId
     */
    String getClientId();
}
