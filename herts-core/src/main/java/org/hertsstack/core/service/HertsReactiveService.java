package org.hertsstack.core.service;

import io.grpc.MethodDescriptor;
import org.hertsstack.core.context.HertsType;

/**
 * Herts reactive service interface
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsReactiveService extends HertsService {

    /**
     * Get implemented herts core type
     *
     * @return HertsType
     */
    HertsType getHertsType();

    /**
     * Get connections
     *
     * @return Connection ids
     */
    String getConnection();

    /**
     * Get grpc method
     *
     * @return MethodDescriptor.MethodType
     */
    MethodDescriptor.MethodType getGrpcMethodType();

    /**
     * Get HertsService.
     *
     * @return HertsService
     */
    Class<?> getService();

    /**
     * Get HertsReceiver
     *
     * @return HertsReceiver
     */
    Class<?> getReceiver();
}
