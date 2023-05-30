package org.herts.common.reactive;

import io.grpc.stub.StreamObserver;

/**
 * Herts reactive streaming internal interface
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsReactiveStreamingInternal {

    /**
     * Register receiver internal of Client
     *
     * @param objectStreamObservers StreamObserver
     */
    void registerReceiver(StreamObserver<Object> objectStreamObservers);
}
