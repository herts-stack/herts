package org.hertsstack.rpcclient;

import io.grpc.ClientInterceptor;

/**
 * Herts core client interceptor
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
interface HertsRpcClientInterceptorBuilder {

    /**
     * Build
     *
     * @return ClientInterceptor
     */
    ClientInterceptor build();
}
