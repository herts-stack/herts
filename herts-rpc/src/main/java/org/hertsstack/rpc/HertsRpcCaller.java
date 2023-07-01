package org.hertsstack.rpc;

import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Herts caller interface
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
interface HertsRpcCaller {

    /**
     * Invoke client and bidirectional streaming method.
     *
     * @param obj              CoreObject
     * @param responseObserver StreamObserver
     * @return StreamObserver
     * @throws InvocationTargetException Failre call method
     * @throws IllegalAccessException    Failure can not access method
     */
    <T> StreamObserver<T> invokeStreaming(Object obj, StreamObserver<T> responseObserver) throws InvocationTargetException, IllegalAccessException;

    /**
     * Invoke server streaming method.
     *
     * @param request          Request generics
     * @param responseObserver StreamObserver
     * @return Object
     * @throws InvocationTargetException Failre call method
     * @throws IllegalAccessException    Failure can not access method
     * @throws IOException               Failure request body
     */
    <T, K> Object invokeServerStreaming(T request, StreamObserver<K> responseObserver) throws InvocationTargetException, IllegalAccessException, IOException;

    /**
     * Invoke server unary method.
     *
     * @param request          Request generics
     * @param responseObserver StreamObserver
     * @return Object
     * @throws InvocationTargetException Failre call method
     * @throws IllegalAccessException    Failure can not access method
     * @throws IOException               Failure request body
     */
    <T, K> Object invokeUnary(T request, StreamObserver<K> responseObserver) throws InvocationTargetException, IllegalAccessException, IOException;
}
