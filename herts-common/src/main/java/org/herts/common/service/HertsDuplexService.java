package org.herts.common.service;

import io.grpc.MethodDescriptor;
import org.herts.common.context.HertsType;

/**
 * Herts service interface
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsDuplexService extends HertsService {

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

    Class<?> getService();

    Class<?> getReceiver();
}
