package com.tomoyane.herts.hertscore;

import io.grpc.Metadata;
import io.grpc.ServerCall;

public interface HertsCoreInterceptor {
    void setResponseMetadata(Metadata metadata);
    <ReqT, RespT> void beforeCallMethod(ServerCall<ReqT, RespT> call, final Metadata requestHeaders);
}
