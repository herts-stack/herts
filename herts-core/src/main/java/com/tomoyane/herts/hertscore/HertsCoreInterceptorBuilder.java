package com.tomoyane.herts.hertscore;

import io.grpc.ServerInterceptor;

/**
 * Herts server interceptor builder
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsCoreInterceptorBuilder {

    /**
     * Build
     * @return ServerInterceptor
     */
    ServerInterceptor build();
}
