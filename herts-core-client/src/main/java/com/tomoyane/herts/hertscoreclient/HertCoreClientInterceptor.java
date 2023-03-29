package com.tomoyane.herts.hertscoreclient;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;

public interface HertCoreClientInterceptor {
    void setRequestMetadata(Metadata metadata);
    <ReqT, RespT> void beforeCallMethod(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel);
}
