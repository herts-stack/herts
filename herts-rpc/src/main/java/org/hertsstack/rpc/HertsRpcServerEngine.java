package org.hertsstack.rpc;

import org.hertsstack.core.context.HertsType;

import io.grpc.Server;

/**
 * Herts server engine
 * @author Herts Contributer
 */
public interface HertsRpcServerEngine {

    /**
     * Start Herts rpc server
     */
    void start();

    /**
     * diStop Herts rpc server
     */
    void stop();

    /**
     * Get grpc Server
     * @return gRPC server
     */
    Server getServer();

    /**
     * Gert herts type of server
     * @return HertsType
     */
    HertsType getHertCoreType();
}
