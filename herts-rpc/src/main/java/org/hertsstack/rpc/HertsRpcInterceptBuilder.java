package org.hertsstack.rpc;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

import org.hertsstack.core.context.SharedServiceContext;

import java.util.logging.Logger;

/**
 * Herts intercept builder
 *
 * @author Herts Contributer
 */
public class HertsRpcInterceptBuilder implements ServerInterceptor {
    private static final Logger logger = Logger.getLogger(HertsRpcInterceptBuilder.class.getName());
    private static final Metadata.Key<String> sessionIdKey = SharedServiceContext.Header.HERTS_CONNECTION_ID;
    private static final Context.Key<String> connectionIdCtxKey = SharedServiceContext.Header.HERTS_CONNECTION_ID_CTX;

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

        String sessionId = requestHeaders.get(sessionIdKey);
        Context ctx = Context.current().withValue(connectionIdCtxKey, sessionId);
        return Contexts.interceptCall(ctx, call, requestHeaders, next);
    }
}