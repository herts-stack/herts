package com.tomoyane.herts.hertscore.engine;

import com.tomoyane.herts.hertscommon.context.HertsType;

import io.grpc.Server;

/**
 * Herts server engine
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsCoreEngine {

    /**
     * Start Herts rpc server
     */
    void start();

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
