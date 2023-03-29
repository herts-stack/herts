package com.tomoyane.herts.hertscoreclient;

import io.grpc.ClientInterceptor;

public interface HertCoreClientInterceptorBuilder {
    ClientInterceptor build();
}
