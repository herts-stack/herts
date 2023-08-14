package org.hertsstack.rpcclient;

import io.grpc.CallCredentials;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.service.HertsService;

import io.grpc.ManagedChannel;

/**
 * Herts core client
 *
 * @author Herts Contributer
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
    ClientRequestInfo getClientConnection();

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
     * Create unknown herts service.
     *
     * @param interfaceClass Herts service interface
     * @return HertsService
     */
    HertsService createUnknownHertsRpcService(Class<?> interfaceClass);

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
