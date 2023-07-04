package org.hertsstack.e2etest.common;

import org.hertsstack.core.logger.Logging;
import org.hertsstack.rpc.HertsRpcInterceptor;
import io.grpc.Metadata;
import io.grpc.ServerCall;

public class GrpcServerInterceptor implements HertsRpcInterceptor {
    private static final java.util.logging.Logger logger = Logging.getLogger(GrpcServerInterceptor.class.getSimpleName());

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
