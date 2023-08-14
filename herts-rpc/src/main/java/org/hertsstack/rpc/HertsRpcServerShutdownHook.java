package org.hertsstack.rpc;

/**
 * Herts rpc serevr shutdown hook trriger
 *
 * @author Herts Contributer
 */
interface HertsRpcServerShutdownHook {

    /**
     * Hook shutdown.
     * getRuntime().addShutdownHook on internal processing
     */
    void hookShutdown();
}
