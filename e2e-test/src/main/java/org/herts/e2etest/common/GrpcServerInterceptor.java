package org.herts.e2etest.common;

import org.herts.core.logger.HertsLogger;
import org.herts.rpc.HertsRpcInterceptor;
import io.grpc.Metadata;
import io.grpc.ServerCall;

import java.util.logging.Logger;

public class GrpcServerInterceptor implements HertsRpcInterceptor {
    private static final Logger logger = HertsLogger.getLogger(GrpcServerInterceptor.class.getSimpleName());

    @Override
    public void setResponseMetadata(Metadata metadata) {
    }

    @Override
    public <ReqT, RespT> void beforeCallMethod(ServerCall<ReqT, RespT> call, Metadata requestHeaders) {
//        logger.info("====== Before call ");
//        logger.info("====== Header "
//                + requestHeaders.get(HEADER_TEST01) + " " + requestHeaders.get(HERTS_HEADER_KEY));
    }
}
