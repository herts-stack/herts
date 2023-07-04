package org.herts.rpcclient;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;

/**
 * Herts core client interceptor
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsRpcClientInterceptor {

    /**
     * Set request header metadata
     *
     * @param metadata Metadata
     */
    void setRequestMetadata(Metadata metadata);

    /**
     * Before call method
     *
     * @param methodDescriptor MethodDescriptor
     * @param callOptions      CallOptions
     * @param channel          Channel
     * @param <ReqT>           Request generics
     * @param <RespT>          Response generics
     */
    <ReqT, RespT> void beforeCallMethod(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel);
}