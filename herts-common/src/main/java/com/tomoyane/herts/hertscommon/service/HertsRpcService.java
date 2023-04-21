package com.tomoyane.herts.hertscommon.service;

import com.tomoyane.herts.hertscommon.context.HertsType;

import io.grpc.MethodDescriptor;

/**
 * Herts core service interface
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsRpcService {

    /**
     * Get implemented herts core type
     * @return HertsType
     */
    HertsType getHertsType();

    /**
     * Get grpc method
     * @return MethodDescriptor.MethodType
     */
    MethodDescriptor.MethodType getGrpcMethodType();

    /**
     * Get rpc connection id list
     * @return Connection list
     */
    String[] getConnections();
}
