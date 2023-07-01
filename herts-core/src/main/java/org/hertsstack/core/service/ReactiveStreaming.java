package org.hertsstack.core.service;

import io.grpc.stub.StreamObserver;

/**
 * Herts reactive streaming internal interface
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface ReactiveStreaming {

    /**
     * Register receiver internal of Client
     *
     * @param objectStreamObservers StreamObserver
     */
    void registerReceiver(StreamObserver<Object> objectStreamObservers);
}
