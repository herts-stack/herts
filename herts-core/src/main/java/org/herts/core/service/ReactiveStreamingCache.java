package org.herts.core.service;

import io.grpc.stub.StreamObserver;

/**
 * HertsReactive cache interface.
 * This class is internal cache service.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
interface ReactiveStreamingCache {

    /**
     * Register StreamingObserver to server cache service.
     * Object parameter is same as Receiver method parameters
     *
     * @param hertsClientId ClientId
     * @param observer      StreamObserver
     */
    void setObserver(String hertsClientId, StreamObserver<Object> observer);

    /**
     * Get observer
     *
     * @param hertsClientId ClientId
     * @return StreamObserver
     */
    StreamObserver<Object> getObserver(String hertsClientId);

    /**
     * Remove Observer.
     *
     * @param hertsClientId ClientId
     * @return Result
     */
    boolean removeObserver(String hertsClientId);

    /**
     * Set clientId.
     *
     * @param clientId clientId
     */
    void setClientId(String clientId);

    /**
     * Get ClientId
     *
     * @param clientId ClientId
     * @return ClientInfo
     */
    String getClientId(String clientId);

    /**
     * Get all client ids.
     * @return Client ids
     */
    String[] getClientIds();

    /**
     * Set Herts receiver.
     *
     * @param clientId      ClientId
     * @param hertsReceiver HertsReceiver
     * @param invoker       HertsReactiveStreamingInvoker
     */
    void setHertsReceiver(String clientId, HertsReceiver hertsReceiver, HertsReactiveStreamingInvoker invoker);

    /**
     * Get Herts receiver.
     *
     * @param clientId ClientId
     * @return HertsReceiverInfo
     */
    HertsReceiverInfo getHertsReceiver(String clientId);
}