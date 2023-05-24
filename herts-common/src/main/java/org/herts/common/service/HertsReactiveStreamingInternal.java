package org.herts.common.service;

import io.grpc.stub.StreamObserver;
import org.herts.common.context.HertsClientInfo;

/**
 * Herts reactive streaming internal interface
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsReactiveStreamingInternal {

    /**
     * Register receiver internal of Client
     * @param clientInfo HertsClientInfo
     * @param objectStreamObservers StreamObserver
     */
    void registerReceiver(HertsClientInfo clientInfo, StreamObserver<Object> objectStreamObservers);
}