package org.herts.common.reactive;

import io.grpc.MethodDescriptor;
import org.herts.common.context.HertsType;
import org.herts.common.service.HertsService;

/**
 * Herts reactive service interface
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsReactiveService extends HertsService {

    /**
     * Get implemented herts core type
     * @return HertsType
     */
    HertsType getHertsType();

    /**
     * Get connections
     * @return Connection ids
     */
    String[] getConnections();

    /**
     * Get grpc method
     * @return MethodDescriptor.MethodType
     */
    MethodDescriptor.MethodType getGrpcMethodType();

    /**
     * Get HertsService.
     * @return HertsService
     */
    Class<?> getService();

    /**
     * Get HertsReceiver
     * @return HertsReceiver
     */
    Class<?> getReceiver();
}
