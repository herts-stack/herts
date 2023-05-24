package org.herts.common.service;

import io.grpc.stub.StreamObserver;
import org.herts.common.context.HertsClientInfo;

/**
 * HertsBroadCaster for internal reactive streaming.
 * Wrapped java.util.logging.Logger
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsBroadCaster {
    /**
     * Get Broadcast receiver interface.
     * The receiver is what you add K in HertsReactiveStreamingService
     * @param clientId ClientId
     * @return Receiver interface
     */
    <K> K broadcast(String clientId);

    /**
     * Register receiver for internal processing.
     * @param clientInfo ClientInfo
     * @param objectStreamObservers StreamObserver
     */
    void registerReceiver(HertsClientInfo clientInfo, StreamObserver<Object> objectStreamObservers);

    /**
     * Set HertsService.
     * @param service HertsService implementation
     */
    void setService(Class<?> service);

    /**
     * Set HertsReceiver
     * @param receiver HertsReceiver implementation
     */
    void setReceiver(Class<?> receiver);

    /**
     * Get HertsService
     * @return HertsService
     */
    Class<?> getService();

    /**
     * Get HertsReceiver
     * @return HertsReceiver
     */
    Class<?> getReceiver();

    /**
     * Get Client id
     * @return ClientId
     */
    String getClientId();
}
