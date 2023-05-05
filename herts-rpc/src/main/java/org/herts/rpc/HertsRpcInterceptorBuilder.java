package org.herts.rpc;

import io.grpc.ServerInterceptor;

/**
 * Herts server interceptor builder
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsRpcInterceptorBuilder {

    /**
     * Build
     * @return ServerInterceptor
     */
    ServerInterceptor build();
}
