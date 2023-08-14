package org.hertsstack.rpc;

import io.grpc.ServerInterceptor;

/**
 * Herts server interceptor builder
 *
 * @author Herts Contributer
 */
public interface HertsRpcInterceptorBuilder {

    /**
     * Build.
     *
     * @return ServerInterceptor
     */
    ServerInterceptor build();
}
