package com.tomoyane.herts.hertsclient;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;

public interface HertClientInterceptor {
    void setRequestMetadata(Metadata metadata);
    <ReqT, RespT> void beforeCallMethod(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel);
}
