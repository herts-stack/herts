package org.hertsstack.core.service;

import io.grpc.stub.StreamObserver;

/**
 * Herts reactive streaming internal interface
 *
 * @author Herts Contributer
 */
public interface ReactiveStreaming {

    /**
     * Register receiver internal of Client
     *
     * @param objectStreamObservers StreamObserver
     */
    void registerReceiver(StreamObserver<Object> objectStreamObservers);
}
