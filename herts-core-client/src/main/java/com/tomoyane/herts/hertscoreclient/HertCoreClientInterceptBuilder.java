package com.tomoyane.herts.hertscoreclient;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;

import static com.tomoyane.herts.hertscommon.context.HertsHeaderContext.CODE_VERSION;
import static com.tomoyane.herts.hertscommon.context.HertsHeaderContext.HERTS_HEADER_KEY;

public class HertCoreClientInterceptBuilder implements ClientInterceptor {
    private final HertCoreClientInterceptor interceptor;

    private HertCoreClientInterceptBuilder(HertCoreClientInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public static class Builder implements HertCoreClientInterceptorBuilder {
        private final HertCoreClientInterceptor interceptor;

        private Builder(HertCoreClientInterceptor interceptor) {
            this.interceptor = interceptor;
        }

        public static Builder create(HertCoreClientInterceptor interceptor) {
            return new Builder(interceptor);
        }

        @Override
        public ClientInterceptor build() {
            return new HertCoreClientInterceptBuilder(this.interceptor);
        }
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {

        if (this.interceptor != null) {
            this.interceptor.beforeCallMethod(methodDescriptor, callOptions, channel);
        }

        return new SimpleForwardingClientCall<>(channel.newCall(methodDescriptor, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                if (interceptor != null) {
                    interceptor.setRequestMetadata(headers);
                }
                headers.put(HERTS_HEADER_KEY, CODE_VERSION);
                super.start(new ForwardingClientCallListener.SimpleForwardingClientCallListener<>(responseListener) {
                    @Override
                    public void onHeaders(Metadata headers) {
                        super.onHeaders(headers);
                    }
                }, headers);
            }
        };
    }
}
