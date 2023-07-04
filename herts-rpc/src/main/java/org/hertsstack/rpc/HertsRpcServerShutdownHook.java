package org.hertsstack.rpc;

/**
 * Herts rpc serevr shutdown hook trriger
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
interface HertsRpcServerShutdownHook {

    /**
     * Hook shutdown.
     * getRuntime().addShutdownHook on internal processing
     */
    void hookShutdown();
}
