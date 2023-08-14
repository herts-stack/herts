package org.hertsstack.core.service;

import org.hertsstack.core.context.HertsType;

import io.grpc.MethodDescriptor;

/**
 * Herts service interface
 *
 * @author Herts Contributer
 */
public interface HertsService {

    /**
     * Get implemented herts core type
     *
     * @return HertsType
     */
    HertsType getHertsType();

    /**
     * Get grpc method
     *
     * @return MethodDescriptor.MethodType
     */
    MethodDescriptor.MethodType getGrpcMethodType();

    /**
     * Get rpc connection id list
     * If you are using HertsHttp, always null
     *
     * @return Connection list
     */
    String getConnection();
}
