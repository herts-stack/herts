package org.hertsstack.rpcclient;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;

class HertsRpcClientEmptyInterceptor implements HertsRpcClientInterceptor {
    @Override
    public void setRequestMetadata(Metadata metadata) {
    }

    @Override
    public <ReqT, RespT> void beforeCallMethod(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
    }
}
