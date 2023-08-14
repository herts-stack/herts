package org.hertsstack.rpc;

import io.grpc.Metadata;
import io.grpc.ServerCall;

/**
 * Herts empty  interceptor
 *
 * @author Herts Contributer
 */
class HertsEmptyRpcInterceptor implements HertsRpcInterceptor {
    private HertsEmptyRpcInterceptor() {
    }

    /**
     * Create factory.
     *
     * @return HertsEmptyRpcInterceptor
     */
    public static HertsEmptyRpcInterceptor create() {
        return new HertsEmptyRpcInterceptor();
    }

    @Override
    public void setResponseMetadata(Metadata metadata) {
    }

    @Override
    public <ReqT, RespT> void beforeCallMethod(ServerCall<ReqT, RespT> call, Metadata requestHeaders) {
    }
}
