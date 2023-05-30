package org.herts.rpcclient;

import org.herts.common.context.HertsType;
import org.herts.common.service.HertsService;

import io.grpc.ManagedChannel;
import org.herts.rpcclient.modelx.ClientConnection;

/**
 * Herts core client
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsRpcClient {

    /**
     * Get connected host
     *
     * @return Connected host
     */
    String getConnectedHost();

    /**
     * Is secure or not
     *
     * @return Result
     */
    boolean isSecureConnection();

    /**
     * Get channel
     *
     * @return ManagedChannel
     */
    ManagedChannel getChannel();

    /**
     * Get client connection
     *
     * @return modelx.ClientConnection
     */
    ClientConnection getClientConnection();

    /**
     * Create herts core interface
     *
     * @param interfaceClass Class type
     * @param <T>            Generics
     * @return HertsCoreService
     */
    <T extends HertsService> T createHertsRpcService(Class<T> interfaceClass);

    /**
     * Get registered herts type
     *
     * @return HertsType
     */
    HertsType getHertsCoreType();
}
