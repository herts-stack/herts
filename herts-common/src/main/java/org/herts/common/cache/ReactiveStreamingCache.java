package org.herts.common.cache;

import io.grpc.stub.StreamObserver;
import org.herts.common.context.HertsClientInfo;

/**
 * HertsReactive cache interface.
 * This class is internal cache service.
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface ReactiveStreamingCache {

    /**
     * Register StreamingObserver to server cache service.
     * Object parameter is same as Receiver method paramaters
     * @param hertsClientId ClientId
     * @param observer StreamObserver
     */
    void registerObserverToServer(String hertsClientId, StreamObserver<Object> observer);

    /**
     * Get observer
     * @param hertsClientId ClientId
     * @return StreamObserver
     */
    StreamObserver<Object> getObserver(String hertsClientId);

    /**
     * Remove Observer.
     * @param hertsClientId ClientId
     * @return Result
     */
    boolean removeObserver(String hertsClientId);

    /**
     * Set ClientInfo.
     * @param clientInfo ClientInfo
     */
    void setClientInfo(HertsClientInfo clientInfo);

    /**
     * Get ClientInfo
     * @param clientId ClientId
     * @return ClientInfo
     */
    HertsClientInfo getClientInfo(String clientId);
}