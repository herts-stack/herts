package org.herts.common.service;

import org.herts.common.context.HertsType;

import io.grpc.MethodDescriptor;

/**
 * Herts service interface
 *
 * @author Herts Contributer
 * @version 1.0.0
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
     *
     * @return Connection list
     */
    String[] getConnections();
}
