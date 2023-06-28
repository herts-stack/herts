package org.herts.rpcclient;

import io.grpc.CallCredentials;
import org.herts.core.context.HertsType;
import org.herts.core.service.HertsService;

import io.grpc.ManagedChannel;

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
     * @return HertsService
     */
    <T extends HertsService> T createHertsRpcService(Class<T> interfaceClass);

    /**
     * Create herts core interface
     *
     * @param interfaceClass Class type
     * @param credentials    CallCredentials if you want set authentication
     * @param <T>            Generics
     * @return HertsService
     */
    <T extends HertsService> T createHertsRpcService(Class<T> interfaceClass, CallCredentials credentials);

    /**
     * Get registered herts type
     *
     * @return HertsType
     */
    HertsType getHertsCoreType();

    /**
     * Get clientId.
     *
     * @return ClientId string
     */
    String getClient();
}
