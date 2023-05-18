package org.herts.rpc;

import io.grpc.Metadata;
import io.grpc.ServerCall;

public class HertsDefaultRpcInterceptor implements HertsRpcInterceptor {
    private HertsDefaultRpcInterceptor() {
    }

    public static HertsDefaultRpcInterceptor create() {
        return new HertsDefaultRpcInterceptor();
    }

    @Override
    public void setResponseMetadata(Metadata metadata) {

    }

    @Override
    public <ReqT, RespT> void beforeCallMethod(ServerCall<ReqT, RespT> call, Metadata requestHeaders) {

    }
}
