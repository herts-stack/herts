package com.tomoyane.herts.hertsclient;

import io.grpc.ClientInterceptor;

public interface HertClientInterceptorBuilder {
    ClientInterceptor build();
}
