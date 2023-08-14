package org.hertsstack.rpcclient;

import io.grpc.ClientInterceptor;

/**
 * Herts core client interceptor
 *
 * @author Herts Contributer
 */
interface HertsRpcClientInterceptorBuilder {

    /**
     * Build
     *
     * @return ClientInterceptor
     */
    ClientInterceptor build();
}
