package org.herts.rpc;

import io.grpc.Metadata;
import io.grpc.ServerCall;

public class HertsEmptyRpcInterceptor implements HertsRpcInterceptor {
    private HertsEmptyRpcInterceptor() {
    }

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
