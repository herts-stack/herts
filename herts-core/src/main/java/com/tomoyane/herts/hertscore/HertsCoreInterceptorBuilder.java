package com.tomoyane.herts.hertscore;

import io.grpc.ServerInterceptor;

public interface HertsCoreInterceptorBuilder {
    ServerInterceptor build();
}
