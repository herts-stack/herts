package org.herts.common.service;

import io.grpc.stub.StreamObserver;

/**
 * Herts client service interface
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsRpcClientService {

    /**
     * Received from server pushed message
     *
     * @param response StreamObserver
     * @param obj      Object
     * @param <T>      Generics
     */
    <T> void received(StreamObserver<T> response, Object obj);
}
