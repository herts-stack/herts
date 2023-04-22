package com.tomoyane.herts.hertsrpcclient;

import com.tomoyane.herts.hertscommon.context.HertsType;
import com.tomoyane.herts.hertscommon.service.HertsRpcService;

import io.grpc.ManagedChannel;

/**
 * Herts core client
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsRpcClient {

    /**
     * Get connected host
     * @return Connected host
     */
    String getConnectedHost();

    /**
     * Is secure or not
     * @return Result
     */
    boolean isSecureConnection();

    /**
     * Get channel
     * @return ManagedChannel
     */
    ManagedChannel getChannel();

    /**
     * Create herts core interface
     * @param classType Class type
     * @return HertsCoreService
     * @param <T> Generics
     */
    <T extends HertsRpcService> T createHertCoreInterface(Class<T> classType);

    /**
     * Get registered herts type
     * @return HertsType
     */
    HertsType getHertsCoreType();
}
