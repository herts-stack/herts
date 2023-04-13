package com.tomoyane.herts.hertscoreclient;

import io.grpc.ClientInterceptor;

/**
 * Herts core client interceptor
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertCoreClientInterceptorBuilder {

    /**
     * Build
     * @return ClientInterceptor
     */
    ClientInterceptor build();
}
