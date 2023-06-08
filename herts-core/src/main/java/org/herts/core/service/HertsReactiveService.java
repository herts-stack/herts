package org.herts.core.service;

import io.grpc.MethodDescriptor;
import org.herts.core.context.HertsType;
import org.herts.core.service.HertsService;

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
