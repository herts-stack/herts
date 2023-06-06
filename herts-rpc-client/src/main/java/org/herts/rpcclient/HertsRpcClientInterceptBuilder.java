package org.herts.rpcclient;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;

public class HertsRpcClientInterceptBuilder implements ClientInterceptor {
    private final HertsRpcClientInterceptor interceptor;

    private HertsRpcClientInterceptBuilder(HertsRpcClientInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public static Builder builder(HertsRpcClientInterceptor interceptor) {
        return new Builder(interceptor);
    }

    public static class Builder implements HertsRpcClientInterceptorBuilder {
        private final HertsRpcClientInterceptor interceptor;

        private Builder(HertsRpcClientInterceptor interceptor) {
            this.interceptor = interceptor;
        }

        public static Builder create(HertsRpcClientInterceptor interceptor) {
            return new Builder(interceptor);
        }

        @Override
        public ClientInterceptor build() {
            return new HertsRpcClientInterceptBuilder(this.interceptor);
        }
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {

        if (this.interceptor != null) {
            this.interceptor.beforeCallMethod(methodDescriptor, callOptions, channel);
        }

        return new SimpleForwardingClientCall<ReqT, RespT>(channel.newCall(methodDescriptor, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                if (interceptor != null) {
                    interceptor.setRequestMetadata(headers);
                }
                super.start(responseListener, headers);
            }
        };
    }
}
