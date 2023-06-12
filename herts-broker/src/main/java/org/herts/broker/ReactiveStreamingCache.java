package org.herts.broker;

import io.grpc.stub.StreamObserver;

/**
 * HertsReactive cache interface.
 * This class is internal cache service.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface ReactiveStreamingCache<T> {

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
     * @param T HertsReceiver
     */
    void setHertsReceiver(String clientId, T hertsReceiver);

    /**
     * Get Herts receiver.
     *
     * @param clientId ClientId
     * @return HertsReceiverInfo
     */
    T getHertsReceiver(String clientId);
}