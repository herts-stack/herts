package org.herts.rpc;

import io.grpc.ForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

import java.util.logging.Logger;

import static org.herts.common.context.HertsSystemContext.Header.CODE_VERSION;
import static org.herts.common.context.HertsSystemContext.Header.HERTS_HEADER_KEY;

public class HertsRpcInterceptBuilder implements ServerInterceptor {
    private static final Logger logger = Logger.getLogger(HertsRpcInterceptBuilder.class.getName());

    private final HertsRpcInterceptor interceptor;

    private HertsRpcInterceptBuilder(HertsRpcInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public static Builder builder(HertsRpcInterceptor interceptor) {
        return new Builder(interceptor);
    }

    public static class Builder implements HertsRpcInterceptorBuilder {
        private final HertsRpcInterceptor interceptor;

        private Builder(HertsRpcInterceptor interceptor) {
            this.interceptor = interceptor;
        }

        @Override
        public ServerInterceptor build() {
            return new HertsRpcInterceptBuilder(this.interceptor);
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