package com.tomoyane.herts.hertscore;

import io.grpc.ForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

import java.util.logging.Logger;

import static com.tomoyane.herts.hertscommon.context.HertsHeaderContext.CODE_VERSION;
import static com.tomoyane.herts.hertscommon.context.HertsHeaderContext.HERTS_HEADER_KEY;

public class HertsCoreInterceptBuilder implements ServerInterceptor {
    private static final Logger logger = Logger.getLogger(HertsCoreInterceptBuilder.class.getName());

    private final HertsCoreInterceptor interceptor;

    private HertsCoreInterceptBuilder(HertsCoreInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public static Builder builder(HertsCoreInterceptor interceptor) {
        return new Builder(interceptor);
    }

    public static class Builder implements HertsCoreInterceptorBuilder {
        private final HertsCoreInterceptor interceptor;

        private Builder(HertsCoreInterceptor interceptor) {
            this.interceptor = interceptor;
        }

        @Override
        public ServerInterceptor build() {
            return new HertsCoreInterceptBuilder(this.interceptor);
        }
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call, final Metadata requestHeaders, ServerCallHandler<ReqT, RespT> next) {

        if (this.interceptor != null) {
            this.interceptor.beforeCallMethod(call, requestHeaders);
        }

        return next.startCall(new ForwardingServerCall.SimpleForwardingServerCall<>(call) {
            @Override
            public void sendHeaders(Metadata responseHeaders) {
                if (interceptor != null) {
                    interceptor.setResponseMetadata(responseHeaders);
                }
                responseHeaders.put(HERTS_HEADER_KEY, CODE_VERSION);
                super.sendHeaders(responseHeaders);
            }
        }, requestHeaders);
    }
}